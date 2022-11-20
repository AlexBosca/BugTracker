package com.example.backend.dao;

import com.example.backend.entity.issue.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

//    @Query("SELECT ")
    Optional<IssueEntity> findByIssueId(String issueId);
}
