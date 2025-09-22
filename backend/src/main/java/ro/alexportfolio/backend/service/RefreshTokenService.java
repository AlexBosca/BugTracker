package ro.alexportfolio.backend.service;

import org.springframework.stereotype.Service;

import ro.alexportfolio.backend.dao.RefreshTokensRepository;
import ro.alexportfolio.backend.model.RefreshToken;
import ro.alexportfolio.backend.model.User;

@Service
public class RefreshTokenService {
    private final RefreshTokensRepository refreshTokensRepository;

    public RefreshTokenService(RefreshTokensRepository refreshTokensRepository) {
        this.refreshTokensRepository = refreshTokensRepository;
    }

    public RefreshToken getRefreshToken(String token) {
        return this.refreshTokensRepository.findByToken(token).orElseThrow(() -> new RuntimeException("No token found"));
    }

    public void createRefreshToken(String token, boolean revoked, User user) {
        RefreshToken refreshToken = new RefreshToken(token, revoked, user);
        refreshTokensRepository.save(refreshToken);
    }

    public void deleteRefreshToken(String token) {
        RefreshToken refreshToken = this.refreshTokensRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("No token found"));

        refreshTokensRepository.delete(refreshToken);
    }
}
