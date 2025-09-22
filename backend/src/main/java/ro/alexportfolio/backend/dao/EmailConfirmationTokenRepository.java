package ro.alexportfolio.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.alexportfolio.backend.model.EmailConfirmationToken;

public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {
    
    Optional<EmailConfirmationToken> findByToken(String token);
}
