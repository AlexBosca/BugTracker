package ro.alexportfolio.backend.unittests.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ro.alexportfolio.backend.service.JwtService;
import ro.alexportfolio.backend.util.RsaKeyUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private RsaKeyUtil rsaKeyUtil;

    @Mock
    private Clock clock;

    private JwtService jwtService;
    private KeyPair keyPair;

    private static final ZonedDateTime NOW = ZonedDateTime.of(2022,
                                                       12,
                                                       26,
                                                       11,
                                                       30,
                                                       0,
                                                       0,
                                                       ZoneId.of("GMT"));

    @BeforeEach
    void setUp() throws Exception {
        // Generate test RSA keypair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();
        // when(rsaKeyUtil.getPublicKey()).thenReturn((RSAPublicKey) keyPair.getPublic());

        jwtService = new JwtService(rsaKeyUtil, clock);
    }

    @Test
    void generateAccessToken_containsCorrectClaims() throws Exception {
        // Given
        String username = "alice";

        // When
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(rsaKeyUtil.getPrivateKey()).thenReturn((RSAPrivateKey) keyPair.getPrivate());

        String token = jwtService.generateAccessToken(username);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .setClock(() -> Date.from(NOW.toInstant()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Then
        assertThat(claims.getSubject()).isEqualTo(username);
        assertThat(claims.getIssuer()).isEqualTo("Bug-Tracker");
        assertThat(claims.getIssuedAt()).isEqualTo(Date.from(NOW.toInstant()));
        assertThat(claims.getExpiration()).isEqualTo(Date.from(NOW.plusSeconds(15).toInstant()));
    }

    @Test
    void generateRefreshToken_containsCorrectExpiration() throws Exception {
        // Given
        String username = "alice";

        // When
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(rsaKeyUtil.getPrivateKey()).thenReturn((RSAPrivateKey) keyPair.getPrivate());

        String token = jwtService.generateRefreshToken(username);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .setClock(() -> Date.from(NOW.toInstant()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Then
        assertThat(claims.getSubject()).isEqualTo(username);
        assertThat(claims.getIssuer()).isEqualTo("Bug-Tracker");
        assertThat(claims.getIssuedAt()).isEqualTo(Date.from(NOW.toInstant()));
        assertThat(claims.getExpiration()).isEqualTo(Date.from(NOW.plusDays(30).toInstant()));
    }

    @Test
    void extractSubject_returnsUsername() throws Exception {
        // Given
        String username = "alice";

        // When
        when(clock.instant()).thenReturn(NOW.toInstant());
        when(rsaKeyUtil.getPublicKey()).thenReturn((RSAPublicKey) keyPair.getPublic());

        // We’ll generate a token with a very long expiration to avoid accidental expiration
        String token = Jwts.builder()
            .setSubject(username)
            .setIssuer("Bug-Tracker")
            .setIssuedAt(Date.from(NOW.toInstant()))
            .setExpiration(Date.from(NOW.toInstant().plus(365, ChronoUnit.DAYS))) // expires in 1 year
            .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
            .compact();

        String extractedUsername = jwtService.extractSubject(token);

        // Then
        assertThat(extractedUsername).isEqualTo(username);
    }
}
