package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

import static javax.persistence.CascadeType.ALL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "teams")
public class TeamEntity extends BaseEntity {

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "teams", cascade = ALL)
    private Collection<ProjectEntity> projects;

    @ManyToMany
    @JoinTable(
            name = "users_in_team",
            joinColumns = @JoinColumn(name = "team_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Collection<UserEntity> colleagues;
}
