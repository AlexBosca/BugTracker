package com.example.backend.dao;

import com.example.backend.entity.issue.IssueCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueCommentRepository extends JpaRepository<IssueCommentEntity, Long> {
}
