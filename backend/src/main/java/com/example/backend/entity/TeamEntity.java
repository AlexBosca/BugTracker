package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "teams")
public class TeamEntity extends BaseEntity {

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(
        mappedBy = "teams",
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST
    )
    private Set<ProjectEntity> projects;

    @ManyToMany
    @JoinTable(
            name = "users_in_team",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<UserEntity> colleagues;

    public void addProject(ProjectEntity project) {
        this.projects.add(project);
        project.getTeams().add(this);
    }
}
