package ro.alexportfolio.backend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ro.alexportfolio.backend.model.UserProjectRole;

@Repository
public interface UserProjectRoleRepository extends JpaRepository<UserProjectRole, Long>  {
    List<UserProjectRole> findByUserId(String userId);
    List<UserProjectRole> findByProjectKey(String projectKey);
    Optional<UserProjectRole> findByUserIdAndProjectKey(String userId, String projectKey);
    boolean existsByUserIdAndProjectKey(String userId, String projectKey);
}
