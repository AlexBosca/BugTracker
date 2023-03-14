package com.example.backend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.backend.entity.UserEntity;

import lombok.RequiredArgsConstructor;

@Repository("user-jpa")
@RequiredArgsConstructor
public class UserJPADataAccessService implements UserDao {

    private final UserRepository userRepository;
    
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
    public void updateUser(UserEntity user) {
        userRepository.save(user);
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
    public int enableUserAccountByEmail(String email) {
        return userRepository.enableAccountByEmail(email);
    }

    @Override
    public int setUserAccountNonExpiredByEmail(String email) {
        return userRepository.setAccountNonExpiredByEmail(email);
    }

    @Override
    public int setUserCredentialsNonExpired(String email) {
        return userRepository.setCredentialsNonExpired(email);
    }

    @Override
    public int unlockUserAccountByEmail(String email) {
        return userRepository.unlockAccountByEmail(email);
    }
}
