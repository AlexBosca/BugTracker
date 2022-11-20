package com.example.backend.entity.issue;

import com.example.backend.entity.BaseEntity;
import com.example.backend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "comments")
public class IssueCommentEntity extends BaseEntity {

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "created_by_user", referencedColumnName = "id")
    private UserEntity createdByUser;

    @ManyToOne
    private IssueEntity createdOnIssue;
}
