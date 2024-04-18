package com.example.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "project_confirmation_tokens")
public class ProjectConfirmationTokenEntity extends BaseEntity {

    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "project_key"
    )
    private ProjectEntity project;

    public ProjectConfirmationTokenEntity(String token,
                                   LocalDateTime createdAt,
                                   LocalDateTime expiresAt,
                                   ProjectEntity project) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.project = project;
    }
}
