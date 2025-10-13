package ro.alexportfolio.backend.controller;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ro.alexportfolio.backend.dto.request.LoginRequestDTO;
import ro.alexportfolio.backend.dto.request.PasswordResetRequestDTO;
import ro.alexportfolio.backend.dto.request.RegistrationRequestDTO;
import ro.alexportfolio.backend.dto.request.ResetRequestDTO;
import ro.alexportfolio.backend.dto.response.JwtResponseDTO;
import ro.alexportfolio.backend.mapper.UserMapper;
import ro.alexportfolio.backend.model.GlobalRole;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.service.JwtService;
import ro.alexportfolio.backend.service.PasswordResetService;
import ro.alexportfolio.backend.service.RefreshTokenService;
import ro.alexportfolio.backend.service.UserService;
import ro.alexportfolio.backend.util.UserIdGenerator;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final int MAX_AGE = 30;

    private final UserService userService;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetService passwordResetService;

    public AuthController(final UserService userServiceParam,
                          final UserMapper mapperParam,
                          final AuthenticationManager authManagerParam,
                          final JwtService jwtServiceParam,
                          final RefreshTokenService refreshTokenServiceParam,
                          final PasswordResetService passwordResetServiceParam) {
        this.userService = userServiceParam;
        this.mapper = mapperParam;
        this.authenticationManager = authManagerParam;
        this.jwtService = jwtServiceParam;
        this.refreshTokenService = refreshTokenServiceParam;
        this.passwordResetService = passwordResetServiceParam;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<JwtResponseDTO> login(final @RequestBody LoginRequestDTO loginRequest) throws Exception {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
            ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(loginRequest.username());

        Set<String> roles = user.getAuthorities()
            .stream()
            .map(Object::toString)
            .collect(Collectors.toSet());

        String refreshToken = jwtService.generateRefreshToken(loginRequest.username());

        this.refreshTokenService.createRefreshToken(refreshToken, false, user);

        JwtResponseDTO response = new JwtResponseDTO(
            accessToken,
            user.getUserId(),
            user.getEmail(),
            roles
        );

        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<Void> logout(final @CookieValue("refreshToken") String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);

        ResponseCookie cookie = clearRefreshTokenCookie();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Void> register(final @RequestBody RegistrationRequestDTO request) {
        User user = mapper.toEntity(request);
        user.setUserId(UserIdGenerator.generateUserId(
                request.firstName(),
                request.lastName()
        ));
        user.setGlobalRole(GlobalRole.USER);
        userService.createUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<String> confirm(final @RequestParam("token") String token) {
        String response = userService.confirmEmail(token);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/refresh")
    public ResponseEntity<String> refreshAccessToken(final @CookieValue("refreshToken") String refreshToken) throws Exception {
        String username = jwtService.extractSubject(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(username);

        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }

    @PostMapping(path = "/password-reset-request")
    public ResponseEntity<Void> passwordResetRequest(final @RequestBody ResetRequestDTO request) {
        passwordResetService.initiatePasswordReset(request.email());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/password-reset")
    public ResponseEntity<Void> validatePasswordResetToken(final @RequestParam("token") String token) {
        passwordResetService.validatePasswordResetToken(token);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/password-reset")
    public ResponseEntity<Void> resetPassword(final @RequestBody PasswordResetRequestDTO request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseCookie createRefreshTokenCookie(final String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/bug-tracker/auth")
                .maxAge(Duration.ofDays(MAX_AGE))
                .build();
    }

    private ResponseCookie clearRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/bug-tracker/auth")
                .maxAge(0)
                .build();
    }
}
