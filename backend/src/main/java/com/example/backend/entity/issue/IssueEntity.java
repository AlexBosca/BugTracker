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
import static javax.persistence.GenerationType.IDENTITY;
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
    @Cascade({ SAVE_UPDATE, MERGE, PERSIST})
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
    public void setUserIdPostPersist() {
        // TODO: Refactor method's body
//        issueId = String.valueOf(project.getName().charAt(0)) + String.valueOf(project.getName().charAt(1)) + "_" + String.format("%05d", this.getId());
        issueId = String.format("%05d", this.getId());

    }
}
