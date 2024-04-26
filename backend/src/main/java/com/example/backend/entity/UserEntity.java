package com.example.backend.entity;

import com.example.backend.config.ClockConfig;
import com.example.backend.entity.issue.IssueCommentEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;
import static javax.persistence.FetchType.EAGER;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, updatable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "expired")
    @Default
    private Boolean isAccountExpired = true;

    @Column(name = "locked")
    @Default
    private Boolean isAccountLocked = true;

    @Column(name = "failed_login_attempts")
    @Default
    private int failedLoginAttempts = 0;

    @Column(name = "credentials_expire_on")
    private LocalDateTime credentialExpiresOn;

    @Column(name = "enabled")
    @Default
    private Boolean isEnabled = false;

    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    private UserRole role;

    @JsonManagedReference
    @OneToMany(
        mappedBy = "createdByUser",
        fetch = EAGER)
    private Collection<IssueEntity> issuesCreated;

    @JsonManagedReference
    @OneToMany(
        mappedBy = "assignedUser",
        fetch = EAGER)
    private Collection<IssueEntity> issuesAssigned;

    @JsonManagedReference
    @OneToMany(
        mappedBy = "closedByUser",
        fetch = EAGER)
    private Collection<IssueEntity> issuesClosed;

    @JsonManagedReference
    @OneToMany(
        mappedBy = "createdByUser",
        fetch = EAGER)
    private Collection<IssueCommentEntity> commentsCreated;
    
    @ManyToMany(mappedBy = "assignedUsers")
    private Set<ProjectEntity> assignedProjects;

    public UserEntity(String firstName,
                   String lastName,
                   String email,
                   String password,
                   UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(role);
        authorities.addAll(role.getGrantedAuthorities());
        return authorities;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isAccountLocked;
    }

    private boolean isPasswordExpired(Clock clock) {
        return (credentialExpiresOn != null) && (credentialExpiresOn.isBefore(now(clock)));
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isPasswordExpired(ClockConfig.getClock());
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @PostPersist
    public void setUserIdPostPersist() {
        userId = String.valueOf(firstName.charAt(0)) + String.valueOf(lastName.charAt(0)) + "_" + String.format("%05d", this.getId());
    }
}
