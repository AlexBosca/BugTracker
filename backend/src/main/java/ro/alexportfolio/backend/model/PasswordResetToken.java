package ro.alexportfolio.backend.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token",
            nullable = false,
            unique = true,
            length = 50)
    private String token;

    @Column(name = "user_id",
            nullable = false,
            length = 50)
    private String userId;

    @Column(name = "expires_at",
            nullable = false)
    private Instant expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id",
                referencedColumnName = "user_id",
                insertable = false,
                updatable = false)
    private User user;

    public PasswordResetToken() {   }

    public PasswordResetToken(final String tokenParam,
                              final User userParam,
                              final Instant expiresAtParam) {
        this.token = tokenParam;
        this.user = userParam;
        this.expiresAt = expiresAtParam;
        this.userId = this.user.getUserId();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(final Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
