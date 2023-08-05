package com.example.backend.service;

import com.example.backend.dao.UserDao;
import com.example.backend.entity.ConfirmationTokenEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.registration.EmailAlreadyConfirmedException;
import com.example.backend.exception.registration.EmailTakenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.time.LocalDateTime.now;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.backend.util.ExceptionUtilities.USER_WITH_EMAIL_NOT_FOUND;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user with email: {}", email);
        
        return userDao
                .selectUserByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, email)));
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

    public void setupAccount(String email) {
        enableAppUser(email);
        unlockAppUser(email);
        setAccountNonExpired(email);
        setCredentialExpiresOn(email);
    }

    public int enableAppUser(String userId) {
        return userDao.enableUserAccountByUserId(userId);
    }

    public int disableAppUser(String userId) {
        return userDao.disableUserAccountByUserId(userId);
    }

    private int unlockAppUser(String email) {
        return userDao.unlockUserAccountByEmail(email);
    }

    private int setAccountNonExpired(String email) {
        return userDao.setUserAccountNonExpiredByEmail(email);
    }

    private int setCredentialExpiresOn(String email) {
        LocalDateTime credentialsExpirationDate = now(clock).plusDays(CREDENTIALS_VALIDITY_IN_DAYS);
        
        return userDao.setUserCrecentialsExpiresOn(email, credentialsExpirationDate);
    }
}
