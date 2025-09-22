package ro.alexportfolio.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, length = 500, unique = true)
    private String token;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "revoked", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean revoked;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    public RefreshToken(String token, boolean revoked, User user) {
        this.token = token;
        this.revoked = revoked;
        this.user = user;
        this.userId = this.user.getUserId();
    }

    public RefreshToken() {
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }
}
