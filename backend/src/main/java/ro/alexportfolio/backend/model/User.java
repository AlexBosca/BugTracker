package ro.alexportfolio.backend.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false, length = 50)
    private String userId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "global_role", nullable = false)
    private GlobalRole globalRole;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    public User() {
    }

    public User(final String userIdParam,
                final String firstNameParam,
                final String lastNameParam,
                final String emailParam,
                final String passwordParam,
                final GlobalRole globalRoleParam) {
        this.userId = userIdParam;
        this.firstName = firstNameParam;
        this.lastName = lastNameParam;
        this.email = emailParam;
        this.password = passwordParam;
        this.globalRole = globalRoleParam;
        this.enabled = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long idParam) {
        this.id = idParam;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userIdParam) {
        this.userId = userIdParam;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstNameParam) {
        this.firstName = firstNameParam;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastNameParam) {
        this.lastName = lastNameParam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String emailParam) {
        this.email = emailParam;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String passwordParam) {
        this.password = passwordParam;
    }

    public GlobalRole getGlobalRole() {
        return globalRole;
    }

    public void setGlobalRole(final GlobalRole globalRoleParam) {
        this.globalRole = globalRoleParam;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAtParam) {
        this.createdAt = createdAtParam;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void enableAccount() {
        this.enabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(this.getGlobalRole().name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
