package com.example.backend.dao;

import com.example.backend.entity.issue.IssueCommentEntity;

public interface IssueCommentDao {
    void insertComment(IssueCommentEntity comment);
}
