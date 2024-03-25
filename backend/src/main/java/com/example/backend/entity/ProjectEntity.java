package com.example.backend.entity;

import com.example.backend.entity.issue.IssueEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

import static javax.persistence.CascadeType.ALL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "projects")
public class ProjectEntity extends BaseEntity {

    @Column(name = "project_key", nullable = false)
    private String projectKey;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "project", cascade = ALL, orphanRemoval = true)
    private Collection<IssueEntity> issues;
}
