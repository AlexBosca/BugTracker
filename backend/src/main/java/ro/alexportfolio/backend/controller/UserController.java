package ro.alexportfolio.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexportfolio.backend.dto.request.UserRequest;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequest request) {
        userService.createUser(
                request.getUserId(),
                request.getEmail(),
                request.getPassword(),
                request.getGlobalRole()
//                request.getFirstName(),
//                request.getLastName()
        );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(
                userService.getAllUsers(),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> getUser(@PathVariable(name = "userId") String userId) {
        return new ResponseEntity<>(
                userService.getUserByUserId(userId),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "/{userID}")
    public ResponseEntity<Void> update(@PathVariable(name = "userId") String usedId, @RequestBody UserRequest request) {
        userService.updateUser(
                usedId,
                request.getEmail(),
                request.getPassword(),
                request.getGlobalRole()
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "userId") String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
