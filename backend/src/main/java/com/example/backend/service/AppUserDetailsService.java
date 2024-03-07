package com.example.backend.service;

import com.example.backend.dao.UserDao;
import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.request.UserRequest;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.backend.util.Utilities.CREDENTIALS_VALIDITY_IN_DAYS;
import static com.example.backend.util.user.UserUtilities.*;


@Slf4j
@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final Clock clock;    

    public AppUserDetailsService(@Qualifier("userJpa") UserDao userDao,
                                 PasswordEncoder passwordEncoder,
                                 ConfirmationTokenService confirmationTokenService,
                                 Clock clock) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.clock = clock;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserCredentialsNotValidException {
        log.info(USER_REQUEST_BY_EMAIL, email);

        UserEntity user = userDao
            .selectUserByEmail(email)
            .orElseThrow(UserCredentialsNotValidException::new);

        log.info(USER_RETURN);
        
        return user;
    }

    public UserEntity loadUserByUserId(String userId) throws UserIdNotFoundException {
        log.info(USER_REQUEST_BY_ID, userId);

        UserEntity user = userDao
            .selectUserByUserId(userId)
            .orElseThrow(UserIdNotFoundException::new);

        log.info(USER_RETURN);
        
        return user;
    }

    public List<UserEntity> loadAllUsers() {
        log.info(USER_REQUEST_ALL);

        List<UserEntity> users = userDao.selectAllUsers();

        log.info(USER_RETURN_ALL);

        return users;
    }

    public List<UserEntity> filterUsers(FilterCriteria filterCriteria) {
        return userDao.selectAllFilteredUsers(filterCriteria);
    }

    public String signUpUser(UserEntity user) {
        log.info(USER_REQUEST_BY_EMAIL, user.getEmail());

        Optional<UserEntity> presentUserOptional = userDao
                .selectUserByEmail(user.getEmail());

        if(presentUserOptional.isPresent()) {
            log.info(USER_RETURN, user.getEmail());
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

    public void saveUser(UserEntity user) {
        setPasswordToUser(user);
        setupAccount(user.getUserId());
    }

    public void updateUser(String userId, UserRequest request) {
        boolean isUserPresent = userDao.existsUserByUserId(userId);

        if(!isUserPresent) {
            throw new UserEmailNotFoundException(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        userDao.updateUser(userId, request);
    }

    public void resetPasswordOfUser(UserEntity user, String currentPassword, String newPassword) {
        if(!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UserCredentialsNotValidException();
        }
        
        user.setPassword(newPassword);
        setPasswordToUser(user);
        setCredentialExpiresOn(user.getUserId());
    }

    public void setPasswordToUser(UserEntity user) {
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

    public void deleteUserByUserId(String userId) {
        boolean isUserPresent = userDao.existsUserByUserId(userId);

        if(!isUserPresent) {
            throw new UserIdNotFoundException(userId);
        }

        userDao.deleteUserByUserId(userId);
    }

    public void setupAccount(String userId) {
        enableAppUser(userId);
        unlockAppUser(userId);
        setAccountNonExpired(userId);
        setCredentialExpiresOn(userId);
    }

    public int enableAppUser(String userId) {
        // boolean isUserPresent = userDao.existsUserByUserId(userId);

        // if(!isUserPresent) {
        //     throw new UserIdNotFoundException(userId);
        // }
        return userDao.enableUserAccountByUserId(userId);
    }

    public int disableAppUser(String userId) {
        // boolean isUserPresent = userDao.existsUserByUserId(userId);

        // if(!isUserPresent) {
        //     throw new UserIdNotFoundException(userId);
        // }
        return userDao.disableUserAccountByUserId(userId);
    }

    public int unlockAppUser(String userId) {
        // boolean isUserPresent = userDao.existsUserByUserId(userId);

        // if(!isUserPresent) {
        //     throw new UserIdNotFoundException(userId);
        // }
        userDao.resetUserAccountFailedLoginAttemptsByUserId(userId);
        return userDao.unlockUserAccountByUserId(userId);
    }

    private int setAccountNonExpired(String userId) {
        // boolean isUserPresent = userDao.existsUserByUserId(userId);

        // if(!isUserPresent) {
        //     throw new UserIdNotFoundException(userId);
        // }
        return userDao.setUserAccountNonExpiredByUserId(userId);
    }

    private int setCredentialExpiresOn(String userId) {
        // boolean isUserPresent = userDao.existsUserByUserId(userId);

        // if(!isUserPresent) {
        //     throw new UserIdNotFoundException(userId);
        // }
        LocalDateTime credentialsExpirationDate = now(clock).plusDays(CREDENTIALS_VALIDITY_IN_DAYS);
        
        return userDao.setUserCrecentialsExpiresOn(userId, credentialsExpirationDate);
    }
}
