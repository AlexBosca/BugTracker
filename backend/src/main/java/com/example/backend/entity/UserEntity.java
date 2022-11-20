package com.example.backend.entity;

import com.example.backend.entity.issue.IssueCommentEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.enums.UserPrivilege;
import com.example.backend.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

import static javax.persistence.CascadeType.ALL;
import static org.hibernate.annotations.CascadeType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(name = "user_id", columnDefinition = "varchar(255)")
    private String userId;

    @Column(name = "first_name", columnDefinition = "varchar(255)", nullable = false)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(255)", nullable = false)
    private String lastName;

    @Column(name = "email", columnDefinition = "varchar(255)", nullable = false, updatable = false, unique = true)
    private String email;

    @Column(name = "password", columnDefinition = "varchar(255)", nullable = false)
    private String password;

    @Column(name = "expired", columnDefinition = "boolean default false")
    private Boolean isAccountExpired;

    @Column(name = "locked", columnDefinition = "boolean default false")
    private Boolean isAccountLocked;

    @Column(name = "credential_expired", columnDefinition = "boolean default false")
    private Boolean isCredentialsExpired;

    @Column(name = "enabled", columnDefinition = "boolean default false")
    private Boolean isEnabled;

    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    private UserRole role;

    @ManyToMany(mappedBy = "colleagues")
    private Collection<TeamEntity> teams;

    @OneToMany(mappedBy = "createdByUser")
    private Collection<IssueEntity> issuesCreated;

    @OneToMany(mappedBy = "assignedUser")
    private Collection<IssueEntity> issuesAssigned;

    @OneToMany(mappedBy = "closedByUser")
    private Collection<IssueEntity> issuesClosed;

    @OneToMany(mappedBy = "createdByUser")
    private Collection<IssueCommentEntity> commentsCreated;

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
        return role.getGrantedAuthorities();
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

    @Override
    public boolean isCredentialsNonExpired() {
        return !isCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @PostPersist
    public void setUserIdPostPersist() {
        // TODO: Refactor method's body
        userId = String.valueOf(firstName.charAt(0)) + String.valueOf(lastName.charAt(0)) + "_" + String.format("%05d", this.getId());
    }
}
