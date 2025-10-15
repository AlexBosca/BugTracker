package ro.alexportfolio.backend.service;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ro.alexportfolio.backend.util.RsaKeyUtil;

@Component
public class JwtService {
    private static final long ACCESS_TOKEN_EXPIRATION_MINUTES = 15;
    private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 30;

    private final RsaKeyUtil rsaKeyUtil;
    private final Clock clock;

    public JwtService(final RsaKeyUtil rsaKeyUtilParam,
                      final Clock clockParam) {
        this.rsaKeyUtil = rsaKeyUtilParam;
        this.clock = clockParam;
    }

    public String generateAccessToken(final String username) throws Exception {
        return Jwts.builder()
            .setSubject(username)
            .setIssuer("Bug-Tracker")
            .setIssuedAt(Date.from(Instant.now(clock)))
            .setExpiration(Date.from(Instant.now(clock).plus(ACCESS_TOKEN_EXPIRATION_MINUTES, ChronoUnit.SECONDS)))
            .signWith(rsaKeyUtil.getPrivateKey(), SignatureAlgorithm.RS256)
            .compact();
    }

    public String generateRefreshToken(final String username) throws Exception {
        return Jwts.builder()
            .setSubject(username)
            .setIssuer("Bug-Tracker")
            .setIssuedAt(Date.from(Instant.now(clock)))
            .setExpiration(Date.from(Instant.now(clock).plus(REFRESH_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS)))
            .signWith(rsaKeyUtil.getPrivateKey(), SignatureAlgorithm.RS256)
            .compact();
    }

    public String extractSubject(final String token) throws Exception  {
        return Jwts.parserBuilder()
            .setSigningKey(rsaKeyUtil.getPublicKey())
            .setClock(() -> Date.from(Instant.now(clock)))
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
