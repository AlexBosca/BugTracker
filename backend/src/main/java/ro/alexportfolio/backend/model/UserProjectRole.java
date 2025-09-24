package ro.alexportfolio.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_project_roles")
public class UserProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @Column(name = "project_key", nullable = false, length = 50)
    private String projectKey;

    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_key", referencedColumnName = "project_key", insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "project_key", referencedColumnName = "project_key", insertable = false, updatable = false)
    @JoinColumn(name = "role_name", referencedColumnName = "role_name", insertable = false, updatable = false)
    private ProjectRole projectRole;

    public UserProjectRole() {}

    public UserProjectRole(final String userIdParam,
                           final String projectKeyParam,
                           final String roleNameParam) {
        this.userId = userIdParam;
        this.projectKey = projectKeyParam;
        this.roleName = roleNameParam;
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

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(final String projectKeyParam) {
        this.projectKey = projectKeyParam;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(final String roleNameParam) {
        this.roleName = roleNameParam;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAtParam) {
        this.createdAt = createdAtParam;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User userParam) {
        this.user = userParam;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(final Project projectParam) {
        this.project = projectParam;
    }

    public ProjectRole getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(final ProjectRole projectRoleParam) {
        this.projectRole = projectRoleParam;
    }
}
