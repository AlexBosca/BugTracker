package com.example.backend.security;

import lombok.RequiredArgsConstructor;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.backend.enums.UserPrivilege;
import com.example.backend.service.AuthenticationService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final AppBasicAuthEntryPoint authenticationEntryPoint;
    private final AuthenticationService authenticationService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http.authorizeHttpRequests(authorize -> authorize
                    .antMatchers("/authentication/account/{userId}/disable").hasAuthority(UserPrivilege.USER_UPDATE.getCode())
                    .antMatchers("/authentication/**").permitAll()
                    .anyRequest().authenticated())
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(accountLockingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(appBasicAuthFilter(), BasicAuthenticationFilter.class)
            .exceptionHandling(handler -> handler.authenticationEntryPoint(authenticationEntryPoint))
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
            .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    AppBasicAuthFilter appBasicAuthFilter() {
        return new AppBasicAuthFilter(authenticationService);
    }

    AccountLockingFilter accountLockingFilter() {
        return new AccountLockingFilter(authenticationService);
    }
}
