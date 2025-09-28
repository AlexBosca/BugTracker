package ro.alexportfolio.backend.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_confirmation_tokens")
public class EmailConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token",
            nullable = false,
            unique = true)
    private String token;

    @Column(name = "user_id",
            nullable = false,
            length = 50)
    private String userId;

    @Column(name = "expires_at",
            nullable = false)
    private Instant expiresAt;

    @Column(name = "used",
            columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean used;

    @OneToOne
    @JoinColumn(name = "user_id",
                referencedColumnName = "user_id",
                insertable = false,
                updatable = false)
    private User user;

    public EmailConfirmationToken() {   }

    public EmailConfirmationToken(final String tokenParam,
                                  final User userParam,
                                  final Instant expiresAtParam,
                                  final boolean usedParam) {
        this.token = tokenParam;
        this.user = userParam;
        this.expiresAt = expiresAtParam;
        this.used = usedParam;
        this.userId = this.user.getUserId();
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String tokenParam) {
        this.token = tokenParam;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userIdParam) {
        this.userId = userIdParam;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(final Instant expiresAtParam) {
        this.expiresAt = expiresAtParam;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(final boolean usedParam) {
        this.used = usedParam;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long idParam) {
        this.id = idParam;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User userParam) {
        this.user = userParam;
    }
}
