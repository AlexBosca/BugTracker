package com.example.backend.service;

import com.example.backend.dto.request.ResetPasswordRequest;
import com.example.backend.entity.ConfirmationTokenEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.exception.registration.EmailAlreadyConfirmedException;
import com.example.backend.exception.token.TokenExpiredException;
import com.example.backend.exception.token.TokenNotFoundException;
import com.example.backend.exception.user.UserCredentialsNotValidException;
import com.example.backend.exception.user.UserIdNotFoundException;
import com.example.backend.exception.user.UserPasswordsNotMatchingException;
import com.example.backend.model.EmailData;
import com.example.backend.model.RegistrationEmailData;
import com.example.backend.util.email.EmailConstants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;

import static com.example.backend.util.Utilities.formattedString;

@Slf4j
@Service
public class AuthenticationService {
    private final AppUserDetailsService userDetailsService;
    private final CredentialValidatorService credentialValidatorService;
    private final EmailSenderService emailSenderService;
    private final ConfirmationTokenService confirmationTokenService;

    @Value("${application.name}")
    private String applicationName;

    @Value("${email.confirmation-link}")
    private String confirmationLink;
    
    public AuthenticationService(AppUserDetailsService userDetailsService,
                                 CredentialValidatorService credentialValidatorService,
                                 @Qualifier("confirmation") EmailSenderService emailSenderService,
                                 ConfirmationTokenService confirmationTokenService) {

        this.userDetailsService = userDetailsService;
        this.credentialValidatorService = credentialValidatorService;
        this.emailSenderService = emailSenderService;
        this.confirmationTokenService = confirmationTokenService;
    }

    public String register(UserEntity user) {
        boolean areCredentialsValid = credentialValidatorService.areCredentialsValid(
                user.getEmail(),
                user.getPassword());

        if(!areCredentialsValid) {
            throw new UserCredentialsNotValidException();
        }

        log.info("Generating token for user: {}", user.getFullName());

        String token = userDetailsService.signUpUser(user);

        log.info("Send confirmation mail to user");

        EmailData emailData = EmailData.builder()
                .recipientName(user.getFullName())
                .recipientEmail(user.getEmail())
                .subject(EmailConstants.EMAIL_ACCOUNT_CONFIRMATION_SUBJECT)
                .title(EmailConstants.EMAIL_ACCOUNT_CONFIRMATION_TITLE)
                .applicationName(applicationName)
                .confirmationLink(Optional.of(formattedString(confirmationLink, token)))
                .notificationContent(Optional.empty())
                .build();

        emailSenderService.send(emailData);

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

        userDetailsService.setupAccount(confirmationToken.getUser().getUserId());

        return "confirmed";
    }

    public void resetPassword(ResetPasswordRequest request) {
        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(request.getEmail());
        boolean isNewPasswordConfirmed = request.getNewPassword().equals(request.getNewPasswordRepeated());
        boolean isNewPasswordValid = credentialValidatorService.isPasswordValid(request.getNewPassword());

        if(!isNewPasswordConfirmed) {
            throw new UserPasswordsNotMatchingException();
        }

        if(!isNewPasswordValid) {
            throw new UserCredentialsNotValidException();
        }

        userDetailsService.resetPasswordOfUser(user, request.getCurrentPassword(), request.getNewPassword());
    }
    
    public void enableAccountByUserId(String userId) {
        int rowsAffected = userDetailsService.enableAppUser(userId);

        if(rowsAffected == 0) {
            throw new UserIdNotFoundException(userId);
        }
    }

    public void disableAccountByUserId(String userId) {
        int rowsAffected = userDetailsService.disableAppUser(userId);

        if(rowsAffected == 0) {
            throw new UserIdNotFoundException(userId);
        }
    }

    public void unlockAccountByUserId(String userId) {
        int rowsAffected = userDetailsService.unlockAppUser(userId);

        if(rowsAffected == 0) {
            throw new UserIdNotFoundException(userId);
        }
    }

    public boolean isAccountLockedByEmail(String email) {
        return userDetailsService.isAccountLocked(email);
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(String authorizationHeader) {
        Credentials credentials = this.extractCredentials(authorizationHeader);

        Collection<? extends GrantedAuthority> authorities = userDetailsService.loadUserByUsername(credentials.getEmail())
                .getAuthorities();

        return new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword(), authorities);
    }

    public String getEmailFromAuthorizationHeader(String authorizationHeader) {
        return this.extractCredentials(authorizationHeader).getEmail();
    }

    private Credentials extractCredentials(String authorizationHeader) {
        String encodedCredentials = authorizationHeader.split(" ")[1];
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
        String[] splitCredentials = decodedCredentials.split(":");

        return new Credentials(splitCredentials[0], splitCredentials[1]);
    }
    
    @Data
    @AllArgsConstructor
    private final class Credentials {
        private final String email;
        private final String password;
    }
}
