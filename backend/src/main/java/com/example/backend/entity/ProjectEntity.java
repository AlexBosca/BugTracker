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

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST
    )
    @JoinTable(
            name = "teams_on_project",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"))
    private Set<TeamEntity> teams = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = ALL, orphanRemoval = true)
    private Collection<IssueEntity> issues;

    public void addTeam(TeamEntity team) {
        team.getProjects().add(this);
        this.teams.add(team);

    }
}
