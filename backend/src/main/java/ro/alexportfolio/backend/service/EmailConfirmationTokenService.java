package ro.alexportfolio.backend.service;

import org.springframework.stereotype.Service;

import ro.alexportfolio.backend.dao.EmailConfirmationTokenRepository;
import ro.alexportfolio.backend.model.EmailConfirmationToken;

@Service
public class EmailConfirmationTokenService {

    private final EmailConfirmationTokenRepository confirmationTokenRepository;

    public EmailConfirmationTokenService(final EmailConfirmationTokenRepository tokenRepositoryParam) {
        this.confirmationTokenRepository = tokenRepositoryParam;
    }

    public EmailConfirmationToken getConfirmationTokenByToken(final String token) {
        return this.confirmationTokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Token not found"));
    }

    public void createEmailConfirmationToken(final EmailConfirmationToken confirmationToken) {
        this.confirmationTokenRepository.save(confirmationToken);
    }
}
