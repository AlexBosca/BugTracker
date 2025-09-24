package ro.alexportfolio.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ro.alexportfolio.backend.security.JwtAuthFilter;
import ro.alexportfolio.backend.security.UserDetailsServiceImpl;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfiguration(final @Lazy UserDetailsServiceImpl service,
                                    final JwtAuthFilter filter) {
        this.userDetailsService = service;
        this.jwtAuthFilter = filter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                    .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure())
                    .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html" ,
                            "/api/v1/users"
                        ).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/**").authenticated()
                        .requestMatchers("/issues/**").authenticated()
                        .requestMatchers("/projects/**").authenticated())
                    .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()))
                    .csrf(AbstractHttpConfigurer::disable)
                    .addFilterBefore(jwtAuthFilter,
                                     UsernamePasswordAuthenticationFilter.class)
                    .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        configuration.setAllowedMethods(List.of("GET",
                                                "POST",
                                                "PUT",
                                                "PATCH",
                                                "DELETE",
                                                "OPTIONS"));

        configuration.setAllowedHeaders(List.of("Authorization",
                                                "Cache-Control",
                                                "Content-Type",
                                                "Access-Control-Allow-Origin"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(final HttpSecurity http) {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        AuthenticationManager authenticationManager = null;

        try {
            authenticationManagerBuilder.userDetailsService(userDetailsService)
                                        .passwordEncoder(passwordEncoder());
            authenticationManager = authenticationManagerBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("Error creating authentication manager");
        }

        return authenticationManager;
    }
}
