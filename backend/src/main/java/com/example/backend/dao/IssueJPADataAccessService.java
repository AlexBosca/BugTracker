package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.filter.FilterUtility;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.DataAccessServiceException;

import lombok.extern.slf4j.Slf4j;

import static com.example.backend.util.database.DatabaseLoggingMessages.*;

@Slf4j
@Repository("issueJpa")
public class IssueJPADataAccessService implements IssueDao {

    private final FilterUtility<IssueEntity> filterUtility;
    private final IssueRepository issueRepository;

    public IssueJPADataAccessService(EntityManager entityManager, IssueRepository issueRepository) {
        this.filterUtility = new FilterUtility<>(entityManager, IssueEntity.class);
        this.issueRepository = issueRepository;
    }

    @Override
    public List<IssueEntity> selectAllIssues() {
        try {
            List<IssueEntity> issues = issueRepository.findAll();
            log.info(ENTITIES_FETCHED, issues);

            return issues;
        } catch (DataAccessException e) {
            log.error(ENTITY_FETCH_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public List<IssueEntity> selectAllFilteredIssues(FilterCriteria filterCriteria) {
        // try {
            List<IssueEntity> filteredIssues = filterUtility.filterEntities(filterCriteria);
            log.info(ENTITIES_FILTERED_FETCHED, filterCriteria);

            return filteredIssues;
        // } catch (DataAccessException e) {
        //     log.error(ENTITY_FETCHED_ERROR, e.getMessage());
        //     throw new DataAccessServiceException();
        // }
    }

    @Override
    public Optional<IssueEntity> selectIssueByIssueId(String issueId) {
        try {
            Optional<IssueEntity> issue = issueRepository.findByIssueId(issueId);

            if(issue.isPresent()) {
                log.info(ENTITY_FETCHED, issueId);
            } else {
                log.warn(ENTITY_NOT_FOUND, issueId);
            }

            return issue;
        } catch (DataAccessException e) {
            log.error(ENTITY_FETCH_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public void insertIssue(IssueEntity issue) {
        try {
            issueRepository.save(issue);
            log.info(ENTITY_SAVED);
        } catch (DataAccessException e) {
            log.error(ENTITY_SAVE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public boolean existsIssueWithIssueId(String issueId) {
        try {
            boolean exists = issueRepository.existsByIssueId(issueId);
            
            if(exists) {
                log.info(ENTITY_EXISTS, issueId);
            } else {
                log.info(ENTITY_NOT_EXISTS, issueId);
            }

            return exists;
        } catch (DataAccessException e) {
            log.error(ENTITY_EXISTS_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public void deleteIssueByIssueId(String issueId) {
        try {
            issueRepository.deleteByIssueId(issueId);
            log.info(ENTITY_DELETED, issueId);
        } catch (DataAccessException e) {
            log.error(ENTITY_DELETE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public void updateIssue(IssueEntity issue) {
        try {
            // issueRepository.update(issue);
            issueRepository.save(issue);
            log.info(ENTITY_UPDATED);
        } catch (DataAccessException e) {
            log.error(ENTITY_UPDATE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }
}
