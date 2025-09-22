package ro.alexportfolio.backend.unittests.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import ro.alexportfolio.backend.service.JwtService;
import ro.alexportfolio.backend.util.RsaKeyUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private RsaKeyUtil rsaKeyUtil;  // mock dependency

    @Mock
    private Clock clock;            // mock dependency

    @InjectMocks
    private JwtService jwtService;  // class under test

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @BeforeEach
    void setUp() throws Exception {
        // Generate an RSA key pair in memory
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        publicKey = (RSAPublicKey) keyPair.getPublic();

        // Mock RsaKeyUtil responses
        when(rsaKeyUtil.getPrivateKey()).thenReturn(privateKey);
        when(rsaKeyUtil.getPublicKey()).thenReturn(publicKey);

        // Mock fixed clock
        Instant fixedInstant = Instant.parse("2025-08-21T12:00:00Z");
        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(ZoneOffset.UTC);
    }

    @Test
    void generateAccessToken_containsCorrectClaims() throws Exception {
        String token = jwtService.generateAccessToken("alice");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("alice");
        assertThat(claims.getIssuer()).isEqualTo("Bug-Tracker");
        assertThat(claims.getIssuedAt()).isEqualTo(Date.from(clock.instant()));
        assertThat(claims.getExpiration()).isEqualTo(Date.from(clock.instant().plusSeconds(15)));
    }

    @Test
    void generateRefreshToken_containsCorrectExpiration() throws Exception {
        String token = jwtService.generateRefreshToken("bob");

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("bob");
        assertThat(claims.getExpiration())
                .isEqualTo(Date.from(clock.instant().plusSeconds(30L * 24 * 60 * 60)));
    }

    @Test
    void extractSubject_returnsUsername() throws Exception {
        String token = jwtService.generateAccessToken("charlie");

        String subject = jwtService.extractSubject(token);

        assertThat(subject).isEqualTo("charlie");
    }
}
