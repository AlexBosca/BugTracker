package ro.alexportfolio.backend.security;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.service.JwtService;
import ro.alexportfolio.backend.service.UserService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthFilter(final JwtService jwtServiceParam,
                         final @Lazy UserService userServiceParam) {
        this.jwtService = jwtServiceParam;
        this.userService = userServiceParam;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");
        if ((header != null) && (header.startsWith("Bearer "))) {
            String token = header.substring(7);

            try {
                String username = jwtService.extractSubject(token);
                User user = userService.getUserByEamil(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
                );

                getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {

            } catch (Exception e) {

            }
        }

        filterChain.doFilter(request, response);
    }
}
