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

    @Autowired
    private UserRepository userRepository;

    public void createUser(String userId, String email, String password, GlobalRole globalRole) {
        User user = new User(userId, email, password, globalRole);

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId).get();
    }

    public void updateUser(String userId, String email, String password, GlobalRole globalRole) {
        User user = userRepository.findByUserId(userId).get();

        user.setEmail(email);
        user.setPassword(password);
        user.setGlobalRole(globalRole);

        userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteByUserId(userId);
    }
}
