package ro.alexportfolio.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ro.alexportfolio.backend.model.Project;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectKey(String projectKey);

    @Transactional
    @Modifying
    void deleteByProjectKey(String projectKey);

    boolean existsByProjectKey(String projectKey);
}
