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
import java.util.Objects;
import java.util.UUID;

import static com.example.backend.util.ExceptionUtilities.*;

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

        boolean userExists = userDao
                .existsUserByEmail(user.getEmail());
        
        if (userExists) {
            log.info("User with email: {}, successfully found", user.getEmail());
            UserEntity presentUser = userDao.selectUserByEmail(user.getEmail()).get();

            if (!presentUser.getFirstName().equals(user.getFirstName()) ||
                !presentUser.getLastName().equals(user.getLastName()) ||
                    !Objects.equals(presentUser.getUserId(), user.getUserId())) {

                throw new EmailTakenException();
            }

            boolean userConfirmationTokenExists = confirmationTokenService
                    .getTokenByUserId(user.getUserId())
                    .isPresent();

            if(userConfirmationTokenExists) {
                ConfirmationTokenEntity userConfirmationToken = confirmationTokenService
                        .getTokenByUserId(user.getUserId())
                        .get();

                if(userConfirmationToken.getConfirmedAt() != null) {
                    throw new EmailAlreadyConfirmedException();
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
        setCredentialsNonExpired(email);
    }

    private int enableAppUser(String email) {
        return userDao.enableUserAccountByEmail(email);
    }

    private int unlockAppUser(String email) {
        return userDao.unlockUserAccountByEmail(email);
    }

    private int setAccountNonExpired(String email) {
        return userDao.setUserAccountNonExpiredByEmail(email);
    }

    private int setCredentialsNonExpired(String email) {
        return userDao.setUserCredentialsNonExpired(email);
    }
}
