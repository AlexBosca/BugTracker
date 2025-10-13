package ro.alexportfolio.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ro.alexportfolio.backend.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
}
