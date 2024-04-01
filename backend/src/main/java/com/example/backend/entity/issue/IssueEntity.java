package com.example.backend.entity.issue;

import com.example.backend.entity.BaseEntity;
import com.example.backend.entity.ProjectEntity;
import com.example.backend.entity.UserEntity;
import com.example.backend.enums.IssuePriority;
import com.example.backend.enums.IssueStatus;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

import static javax.persistence.CascadeType.ALL;
import static org.hibernate.annotations.CascadeType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "issues")
public class IssueEntity extends BaseEntity {

    @Column(name = "issue_id")
    private String issueId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "reproducing_steps")
    private String reproducingSteps;

    @Column(name = "environment")
    private String environment;

    @Column(name = "version")
    private String version;

    @Column(name = "status")
    private IssueStatus status;

    @Column(name = "priority")
    private IssuePriority priority;

    @ManyToOne
    @JoinColumn(name = "created_by_user", referencedColumnName = "id")
    @Cascade({SAVE_UPDATE, MERGE, PERSIST})
    private UserEntity createdByUser;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "assigned_user", referencedColumnName = "id")
    @Cascade({ SAVE_UPDATE, MERGE, PERSIST})
    private UserEntity assignedUser;

    @Column(name = "assigned_on")
    private LocalDateTime assignedOn;

    @ManyToOne
    @JoinColumn(name = "modified_by_user", referencedColumnName = "id")
    @Cascade({ SAVE_UPDATE, MERGE, PERSIST})
    private UserEntity modifiedByUser;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @ManyToOne
    @JoinColumn(name = "closed_by_user", referencedColumnName = "id")
    @Cascade({ SAVE_UPDATE, MERGE, PERSIST})
    private UserEntity closedByUser;

    @Column(name = "closed_on")
    private LocalDateTime closedOn;

    @ManyToOne
    @JoinColumn(name = "tester", referencedColumnName = "id")
    private UserEntity tester;

    @OneToMany(mappedBy = "createdOnIssue", cascade = ALL, orphanRemoval = true)
    private Collection<IssueCommentEntity> discussion;
    
    @ManyToOne
    private ProjectEntity project;

    @PostPersist
    public void setIssueIdPostPersist() {
        issueId = String.format("%s-%04d", this.getProject().getProjectKey(), this.getId());
    }

    @Override
    public String toString() {
        return "IssueEntity [issueId=" + issueId + ", title=" + title + ", description=" + description
                + ", reproducingSteps=" + reproducingSteps + ", environment=" + environment + ", version=" + version
                + ", status=" + status + ", priority=" + priority + ", createdByUser=" + createdByUser.getUserId() + ", createdOn="
                + createdOn + ", assignedUser=" + assignedUser + ", assignedOn=" + assignedOn + ", modifiedByUser="
                + modifiedByUser + ", modifiedOn=" + modifiedOn + ", closedByUser=" + closedByUser + ", closedOn="
                + closedOn + ", tester=" + tester + ", discussion=" + discussion + ", project=" + project + "]";
    }

    
}
