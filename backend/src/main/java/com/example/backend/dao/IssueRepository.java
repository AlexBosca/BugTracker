package com.example.backend.dao;

import com.example.backend.entity.issue.IssueEntity;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<IssueEntity, Long>, JpaSpecificationExecutor<IssueEntity> {

    List<IssueEntity> findAll(Specification<IssueEntity> specification);

//    @Query("SELECT ")
    Optional<IssueEntity> findByIssueId(String issueId);

    boolean existsByIssueId(String issueId);

    @Modifying
    void deleteByIssueId(String issueId);

    // @Modifying
    // void update(IssueEntity issue);
}
