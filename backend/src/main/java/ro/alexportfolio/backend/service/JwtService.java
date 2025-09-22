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
    private final RsaKeyUtil rsaKeyUtil;
    private final Clock clock;

    public JwtService(RsaKeyUtil rsaKeyUtil, Clock clock) {
        this.rsaKeyUtil = rsaKeyUtil;
        this.clock = clock;
    }

    public String generateAccessToken(String username) throws Exception {
        return Jwts.builder()
            .setSubject(username)
            .setIssuer("Bug-Tracker")
            .setIssuedAt(Date.from(Instant.now(clock)))
            .setExpiration(Date.from(Instant.now(clock).plus(15, ChronoUnit.SECONDS)))
            .signWith(rsaKeyUtil.getPrivateKey(), SignatureAlgorithm.RS256)
            .compact();
    }

    public String generateRefreshToken(String username) throws Exception {
        return Jwts.builder()
            .setSubject(username)
            .setIssuer("Bug-Tracker")
            .setIssuedAt(Date.from(Instant.now(clock)))
            .setExpiration(Date.from(Instant.now(clock).plus(30, ChronoUnit.DAYS)))
            .signWith(rsaKeyUtil.getPrivateKey(), SignatureAlgorithm.RS256)
            .compact();
    }

    public String extractSubject(String token) throws Exception  {
        return Jwts.parserBuilder()
            .setSigningKey(rsaKeyUtil.getPublicKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
