package com.example.backend.service;

import com.example.backend.dao.UserRepository;
import com.example.backend.entity.ConfirmationTokenEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.registration.EmailAlreadyConfirmedException;
import com.example.backend.exception.registration.EmailTakenException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.example.backend.util.ExceptionUtilities.*;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

//    private final static String USER_NOT_FOUND_MSG =
//            "user with email %s not found";

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_WITH_EMAIL_NOT_FOUND, email)));
    }

    public String signUpUser(UserEntity user) {
        boolean userExists = userRepository
                .findByEmail(user.getEmail())
                .isPresent();

        if (userExists) {
            UserEntity presentUser = userRepository.findByEmail(user.getEmail()).get();

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

        saveUser(user);

        return createConfirmationToken(user);
    }

    private void saveUser(UserEntity user) {
        String encodedPassword = passwordEncoder
                .encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    private String createConfirmationToken(UserEntity user) {
        String token = UUID.randomUUID().toString();

        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
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
        return userRepository.enableAppUser(email);
    }

    private int unlockAppUser(String email) {
        return userRepository.unlockAppUser(email);
    }

    private int setAccountNonExpired(String email) {
        return userRepository.setAccountNonExpired(email);
    }

    private int setCredentialsNonExpired(String email) {
        return userRepository.setCredentialsNonExpired(email);
    }
}
