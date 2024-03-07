package com.example.backend.dao;

import org.springframework.stereotype.Repository;

import com.example.backend.entity.issue.IssueCommentEntity;

import lombok.RequiredArgsConstructor;

@Repository("commentJpa")
@RequiredArgsConstructor
public class IssueCommentJPADataAccessService implements IssueCommentDao {

    private final IssueCommentRepository commentRepository;

    @Override
    public void insertComment(IssueCommentEntity comment) {
        commentRepository.save(comment);
    }
}
