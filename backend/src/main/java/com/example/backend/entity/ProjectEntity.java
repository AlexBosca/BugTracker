package com.example.backend.entity;

import com.example.backend.entity.issue.IssueEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST
    )
    @JoinTable(
            name = "teams_on_project",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"))
    private Set<TeamEntity> teams;

    @OneToMany(mappedBy = "project", cascade = ALL, orphanRemoval = true)
    private Collection<IssueEntity> issues;

    public void addTeam(TeamEntity team) {
        team.getProjects().add(this);
        this.teams.add(team);

    }
}
