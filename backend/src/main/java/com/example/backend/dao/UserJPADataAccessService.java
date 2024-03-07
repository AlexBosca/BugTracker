package com.example.backend.dao;

import static com.example.backend.util.database.DatabaseLoggingMessages.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.filter.FilterUtility;
import com.example.backend.dto.request.UserRequest;
import com.example.backend.entity.UserEntity;
import com.example.backend.entity.issue.IssueEntity;
import com.example.backend.exception.DataAccessServiceException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository("userJpa")
public class UserJPADataAccessService implements UserDao {

    private final FilterUtility<UserEntity> filterUtility;
    private final UserRepository userRepository;
    
    public UserJPADataAccessService(EntityManager entityManager, UserRepository userRepository) {
        this.filterUtility = new FilterUtility<>(entityManager, UserEntity.class);
        this.userRepository = userRepository;
    }

    @Override
    public void deleteUserByUserId(String userId) {
        try {
            userRepository.deleteByUserId(userId);
            log.info(ENTITY_DELETED, userId);
        } catch (DataAccessException e) {
            log.error(ENTITY_DELETE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public boolean existsUserByUserId(String userId) {
        try {
            boolean exists = userRepository.existsByUserId(userId);
            
            if(exists) {
                log.info(ENTITY_EXISTS, userId);
            } else {
                log.info(ENTITY_NOT_EXISTS, userId);
            }

            return exists;
        } catch (DataAccessException e) {
            log.error(ENTITY_EXISTS_ERROR, userId, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public void insertUser(UserEntity user) {
        try {
            userRepository.save(user);
            log.info(ENTITY_SAVED);
        } catch (DataAccessException e) {
            log.error(ENTITY_SAVE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public List<UserEntity> selectAllUsers() {
        try {
            List<UserEntity> issues = userRepository.findAll();
            log.info(ENTITIES_FETCHED, issues);

            return issues;
        } catch (DataAccessException e) {
            log.error(ENTITY_FETCH_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public Optional<UserEntity> selectUserByUserId(String userId) {
        try {
            Optional<UserEntity> user = userRepository.findByUserId(userId);

            if(user.isPresent()) {
                log.info(ENTITY_FETCHED, userId);
            } else {
                log.warn(ENTITY_NOT_FOUND, userId);
            }

            return user;
        } catch (DataAccessException e) {
            log.error(ENTITY_FETCH_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public List<UserEntity> selectAllFilteredUsers(FilterCriteria filterCriteria) {
        // try {
            List<UserEntity> filteredUsers = filterUtility.filterEntities(filterCriteria);
            log.info(ENTITIES_FILTERED_FETCHED, filterCriteria);

            return filteredUsers;
        // } catch (DataAccessException e) {
        //     log.error(ENTITY_FETCHED_ERROR, e.getMessage());
        //     throw new DataAccessServiceException();
        // }
    }

    @Override
    public void updateUser(String userId, UserRequest request) {
        try {
            userRepository.updateUser(
                userId,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
            );
            log.info(ENTITY_UPDATED);
        } catch (DataAccessException e) {
            log.error(ENTITY_UPDATE_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public boolean existsUserByEmail(String email) {
        try {
            boolean exists = userRepository.existsByEmail(email);
            
            if(exists) {
                log.info(ENTITY_EXISTS, email);
            } else {
                log.info(ENTITY_NOT_EXISTS, email);
            }

            return exists;
        } catch (DataAccessException e) {
            log.error(ENTITY_EXISTS_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public Optional<UserEntity> selectUserByEmail(String email) {
        try {
            Optional<UserEntity> user = userRepository.findByEmail(email);

            if(user.isPresent()) {
                log.info(ENTITY_FETCHED, email);
            } else {
                log.warn(ENTITY_NOT_FOUND, email);
            }

            return user;
        } catch (DataAccessException e) {
            log.error(ENTITY_FETCH_ERROR, e.getMessage());
            throw new DataAccessServiceException();
        }
    }

    @Override
    public int enableUserAccountByUserId(String userId) {
        return userRepository.enableAccountByUserId(userId);
    }

    @Override
    public int disableUserAccountByUserId(String userId) {
        return userRepository.disableAccountByUserId(userId);
    }

    @Override
    public int setUserAccountNonExpiredByUserId(String userId) {
        return userRepository.setAccountNonExpiredByUserId(userId);
    }

    @Override
    public int unlockUserAccountByUserId(String userId) {
        return userRepository.unlockAccountByUserId(userId);
    }

    @Override
    public int lockUserAccountByEmail(String email) {
        return userRepository.lockAccountByEmail(email);
    }

    @Override
    public int setUserAccountFailedLoginAttemptsByEmail(String email, int attempts) {
        return userRepository.setAccountFailedLoginAttemptsByEmail(email, attempts);
    }

    @Override
    public int resetUserAccountFailedLoginAttemptsByEmail(String email) {
        return userRepository.resetAccountFailedLoginAttemptsByEmail(email);
    }

    @Override
    public int resetUserAccountFailedLoginAttemptsByUserId(String userId) {
        return userRepository.resetAccountFailedLoginAttemptsByUserId(userId);
    }

    @Override
    public boolean isUserAccountLockedByEmail(String email) {
        return userRepository.isAccountLockedByEmail(email);
    }

    @Override
    public int setUserCrecentialsExpiresOn(String userId, LocalDateTime credentialsExpirationDate) {
        return userRepository.setCrecentialExpiresOn(userId, credentialsExpirationDate);
    }
}
