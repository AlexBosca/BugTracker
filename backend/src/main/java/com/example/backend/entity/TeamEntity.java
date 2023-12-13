package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TeamEntity other = (TeamEntity) obj;
        if (teamId == null) {
            if (other.teamId != null)
                return false;
        } else if (!teamId.equals(other.teamId))
            return false;
        return true;
    }

    
}
