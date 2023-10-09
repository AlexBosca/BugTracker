package com.example.backend.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.backend.dto.filter.FilterCriteria;
import com.example.backend.dto.filter.FilterUtility;
import com.example.backend.dto.request.UserRequest;
import com.example.backend.entity.UserEntity;

@Repository("user-jpa")
public class UserJPADataAccessService implements UserDao {

    private final FilterUtility<UserEntity> filterUtility;
    private final UserRepository userRepository;
    
    public UserJPADataAccessService(EntityManager entityManager, UserRepository userRepository) {
        this.filterUtility = new FilterUtility<>(entityManager, UserEntity.class);
        this.userRepository = userRepository;
    }

    @Override
    public void deleteUserByUserId(String userId) {
        userRepository.deleteByUserId(userId);
    }

    @Override
    public boolean existsUserByUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public void insertUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public List<UserEntity> selectAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserEntity> selectUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public List<UserEntity> selectAllFilteredUsers(FilterCriteria filterCriteria) {
        return filterUtility.filterEntities(filterCriteria);
    }

    @Override
    public void updateUser(String userId, UserRequest request) {
        userRepository.updateUser(
            userId,
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getPassword()
        );
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserEntity> selectUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
