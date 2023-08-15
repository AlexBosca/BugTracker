package com.example.backend.service;

import com.example.backend.dao.UserDao;
import com.example.backend.entity.ConfirmationTokenEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.registration.EmailAlreadyConfirmedException;
import com.example.backend.exception.registration.EmailTakenException;
import com.example.backend.exception.user.UserCredentialsNotValidException;
import com.example.backend.exception.user.UserEmailNotFoundException;
import com.example.backend.exception.user.UserIdNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.backend.util.Utilities.CREDENTIALS_VALIDITY_IN_DAYS;


@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    @Qualifier("user-jpa")
    private final UserDao userDao;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final ConfirmationTokenService confirmationTokenService;
    @Autowired
    private final Clock clock;

    @Override
    public UserDetails loadUserByUsername(String email) throws UserCredentialsNotValidException {
        log.info("Loading user with email: {}", email);
        
        return userDao
                .selectUserByEmail(email)
                .orElseThrow(UserCredentialsNotValidException::new);
    }

    public UserEntity loadUserByUserId(String userId) throws UserIdNotFoundException {
        log.info("Loading user with userId: {}", userId);

        return userDao
                .selectUserByUserId(userId)
                .orElseThrow(UserIdNotFoundException::new);
    }

    public String signUpUser(UserEntity user) {
        log.info("Searching for user with email: {}", user.getEmail());

        Optional<UserEntity> presentUserOptional = userDao
                .selectUserByEmail(user.getEmail());

        if(presentUserOptional.isPresent()) {
            log.info("User with email: {}, successfully found", user.getEmail());
            UserEntity presentUser = presentUserOptional.get();

            if (!presentUser.getFirstName().equals(user.getFirstName()) ||
                !presentUser.getLastName().equals(user.getLastName())) {

                throw new EmailTakenException();
            }

            Optional<ConfirmationTokenEntity> confirmationTokenOptional = confirmationTokenService
                    .getTokenByUserId(presentUser.getUserId());

            if(confirmationTokenOptional.isPresent()) {
                ConfirmationTokenEntity userConfirmationToken = confirmationTokenOptional.get();

                if(userConfirmationToken.getConfirmedAt() != null) {
                    throw new EmailAlreadyConfirmedException();
                }

                LocalDateTime expiresAt = userConfirmationToken.getExpiresAt();

                if(expiresAt.isBefore(now(clock))) {
                    userConfirmationToken
                            .setExpiresAt(now(clock).plusMinutes(5));

                    // TODO: Create specific function to update the expiration date
                    confirmationTokenService.saveConfirmationToken(userConfirmationToken);

                    return userConfirmationToken.getToken();
                }

                return userConfirmationToken.getToken();
            }

            return createConfirmationToken(user);
        }

        setPasswordToUser(user);

        return createConfirmationToken(user);
    }

    public void resetPasswordOfUser(UserEntity user, String currentPassword, String newPassword) {
        if(!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserCredentialsNotValidException();
        }
        
        user.setPassword(newPassword);
        setPasswordToUser(user);
        setCredentialExpiresOn(user.getUserId());
    }

    private void setPasswordToUser(UserEntity user) {
        String encodedPassword = passwordEncoder
                .encode(user.getPassword());

        user.setPassword(encodedPassword);

        userDao.insertUser(user);
    }

    private String createConfirmationToken(UserEntity user) {
        String token = UUID.randomUUID().toString();

        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(
                token,
                now(clock),
                now(clock).plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public void handleFailedLoginAttempt(String email) {
        UserEntity user = userDao.selectUserByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        int loginAttempts = user.getFailedLoginAttempts() + 1;
        userDao.setUserAccountFailedLoginAttemptsByEmail(email, loginAttempts);
        if(loginAttempts >= 3) {
            userDao.lockUserAccountByEmail(email);
        }
    }

    public void resetFailedLoginAttempts(String email) {
        boolean isUserPresent = userDao.existsUserByEmail(email);

        if(!isUserPresent) {
            throw new UserEmailNotFoundException(email);
        }

        userDao.resetUserAccountFailedLoginAttemptsByEmail(email);
    }

    public boolean isAccountLocked(String email) {
        boolean isUserPresent = userDao.existsUserByEmail(email);

        if(!isUserPresent) {
            throw new UserEmailNotFoundException(email);
        }

        return userDao.isUserAccountLockedByEmail(email);
    }

    public void setupAccount(String userId) {
        enableAppUser(userId);
        unlockAppUser(userId);
        setAccountNonExpired(userId);
        setCredentialExpiresOn(userId);
    }

    public int enableAppUser(String userId) {
        boolean isUserPresent = userDao.existsUserByUserId(userId);

        if(!isUserPresent) {
            throw new UserIdNotFoundException(userId);
        }
        return userDao.enableUserAccountByUserId(userId);
    }

    public int disableAppUser(String userId) {
        boolean isUserPresent = userDao.existsUserByUserId(userId);

        if(!isUserPresent) {
            throw new UserIdNotFoundException(userId);
        }
        return userDao.disableUserAccountByUserId(userId);
    }

    public int unlockAppUser(String userId) {
        boolean isUserPresent = userDao.existsUserByUserId(userId);

        if(!isUserPresent) {
            throw new UserIdNotFoundException(userId);
        }
        userDao.resetUserAccountFailedLoginAttemptsByUserId(userId);
        return userDao.unlockUserAccountByUserId(userId);
    }

    private int setAccountNonExpired(String userId) {
        boolean isUserPresent = userDao.existsUserByUserId(userId);

        if(!isUserPresent) {
            throw new UserIdNotFoundException(userId);
        }
        return userDao.setUserAccountNonExpiredByUserId(userId);
    }

    private int setCredentialExpiresOn(String userId) {
        boolean isUserPresent = userDao.existsUserByUserId(userId);

        if(!isUserPresent) {
            throw new UserIdNotFoundException(userId);
        }
        LocalDateTime credentialsExpirationDate = now(clock).plusDays(CREDENTIALS_VALIDITY_IN_DAYS);
        
        return userDao.setUserCrecentialsExpiresOn(userId, credentialsExpirationDate);
    }
}
