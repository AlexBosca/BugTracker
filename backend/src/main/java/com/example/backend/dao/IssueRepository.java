package com.example.backend.dao;

import com.example.backend.entity.issue.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

//    @Query("SELECT ")
    Optional<IssueEntity> findByIssueId(String issueId);

    boolean existsByIssueId(String issueId);

    @Modifying
    void deleteByIssueId(String issueId);

    // @Modifying
    // void update(IssueEntity issue);
}
