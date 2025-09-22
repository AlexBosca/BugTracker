package ro.alexportfolio.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.alexportfolio.backend.dto.request.UserRequestDTO;
import ro.alexportfolio.backend.dto.response.UserResponseDTO;
import ro.alexportfolio.backend.mapper.UserMapper;
import ro.alexportfolio.backend.model.User;
import ro.alexportfolio.backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    public UserController(UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDTO request) {
        userService.createUser(mapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return new ResponseEntity<>(
                mapper.toResponseList(userService.getAllUsers()),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable(name = "userId") String userId) {
        return new ResponseEntity<>(
                mapper.toResponse(userService.getUserByUserId(userId)),
                HttpStatus.OK
        );
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<Void> update(@PathVariable(name = "userId") String usedId,
                                       @RequestBody UserRequestDTO request) {
        userService.updateUser(usedId, mapper.toEntity(request));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "userId") String userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
