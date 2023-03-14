package com.example.backend.service;

import com.example.backend.dto.request.RegistrationRequest;
import com.example.backend.entity.ConfirmationTokenEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.token.TokenExpiredException;
import com.example.backend.exception.token.TokenNotFoundException;
import com.example.backend.exception.registration.EmailAlreadyConfirmedException;
import com.example.backend.exception.user.UserCredentialsNotValidException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.example.backend.util.Utilities.CONFIRMATION_LINK;
import static com.example.backend.util.Utilities.formattedString;
import static com.example.backend.enums.UserRole.getRoleByCode;

//TODO: Add logging wherever it is neccessary
@Slf4j
@Service
@AllArgsConstructor
public class RegistrationService {
    
    @Autowired
    private final AppUserDetailsService userDetailsService;
    @Autowired
    private final CredentialValidatorService credentialValidatorService;
    @Autowired
    private final EmailSenderServiceImpl emailSenderService;
    @Autowired
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean areCredentialsValid = credentialValidatorService.isValid(
                request.getEmail(),
                request.getPassword());

        if(!areCredentialsValid) {
            throw new UserCredentialsNotValidException();
        }

        log.info("Generating token for user: {}", request.getFullName());

        String token = userDetailsService.signUpUser(
                UserEntity.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .role(getRoleByCode(request.getRole())).build());

        log.info("Send confirmation mail to user");

        emailSenderService.send(
                request.getFullName(),
                request.getEmail(),
                // buildEmail(request.getFullName(), formattedString(CONFIRMATION_LINK, token))
                formattedString(CONFIRMATION_LINK, token));

        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(TokenNotFoundException::new);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        confirmationTokenService.setConfirmedAt(token);

        userDetailsService.setupAccount(confirmationToken.getUser().getEmail());

        return "confirmed";
    }
}
