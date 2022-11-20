package com.example.backend.dao;

import com.example.backend.entity.issue.IssueCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueCommentRepository extends JpaRepository<IssueCommentEntity, Long> {
}
