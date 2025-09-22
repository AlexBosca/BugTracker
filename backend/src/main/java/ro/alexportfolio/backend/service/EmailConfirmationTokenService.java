package ro.alexportfolio.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ro.alexportfolio.backend.dao.EmailConfirmationTokenRepository;
import ro.alexportfolio.backend.model.EmailConfirmationToken;

@Service
public class EmailConfirmationTokenService {
    
    private final EmailConfirmationTokenRepository confirmationTokenRepository;

    public EmailConfirmationTokenService(EmailConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public EmailConfirmationToken getConfirmationTokenByToken(String token) {
        return this.confirmationTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public void createEmailConfirmationToken(EmailConfirmationToken confirmationToken) {
        this.confirmationTokenRepository.save(confirmationToken);
    }

    // private final List<EmailConfirmationToken> emailConfirmationTokens = new ArrayList<>();

    // public EmailConfirmationToken findByToken(String token) {
    //     return emailConfirmationTokens.stream()
    //         .filter(t -> t.getToken().equals(token))
    //         .findFirst()
    //         .orElseThrow(() -> new RuntimeException("No token found"));
    // }

    // public void save(EmailConfirmationToken confirmationToken) {
    //     this.emailConfirmationTokens.add(confirmationToken);
    // }

    // public void update(EmailConfirmationToken confirmationToken) {
    //     emailConfirmationTokens.stream()
    //         .filter(t -> t.getToken().equals(confirmationToken.getToken()))
    //         .findFirst()
    //         .ifPresent(existingToken -> {
    //             int index = emailConfirmationTokens.indexOf(existingToken);
    //             emailConfirmationTokens.set(index, confirmationToken);
    //         });
    // }
}
