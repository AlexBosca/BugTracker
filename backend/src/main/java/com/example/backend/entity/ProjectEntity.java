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

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(
            name = "teams_on_project",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"))
    private Collection<TeamEntity> teams;

    @OneToMany(mappedBy = "project", cascade = ALL, orphanRemoval = true)
    private Collection<IssueEntity> issues;
}
