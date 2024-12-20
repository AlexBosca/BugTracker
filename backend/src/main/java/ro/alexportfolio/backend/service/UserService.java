package ro.alexportfolio.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ro.alexportfolio.backend.dao.UserRepository;
import ro.alexportfolio.backend.model.GlobalRole;
import ro.alexportfolio.backend.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public void updateUser(String userId, User user) {
        User existingUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setGlobalRole(user.getGlobalRole());

        userRepository.save(existingUser);
    }

    public void deleteUser(String userId) {
        userRepository.deleteByUserId(userId);
    }
}
