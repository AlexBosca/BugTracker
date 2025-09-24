package ro.alexportfolio.backend.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ro.alexportfolio.backend.model.RefreshToken;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    // private final Set<RefreshToken> refreshTokens = new HashSet<>();

    // public RefreshToken findByToken(String token) {
    //     return refreshTokens.stream().filter(r -> r.getToken().equals(token)).findFirst().orElseThrow(() -> new RuntimeException("No token found"));
    // }

    // public void save(RefreshToken refreshToken) {
    //     this.refreshTokens.add(refreshToken);
    // }

    // public void delete(String token) {
    //     RefreshToken refreshToken = refreshTokens.stream().filter(r -> r.getToken().equals(token)).findFirst().orElseThrow(() -> new RuntimeException("No token found"));
    //     this.refreshTokens.remove(refreshToken);
    // }
}
