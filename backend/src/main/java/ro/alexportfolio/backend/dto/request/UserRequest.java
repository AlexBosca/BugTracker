package ro.alexportfolio.backend.dto.request;

import ro.alexportfolio.backend.model.GlobalRole;

import java.time.LocalDateTime;

public class UserRequest {

    private String userId;
    private String email;
    private String password;
    private GlobalRole globalRole;
//    private LocalDateTime createdAt;
//    private String firstName;
//    private String lastName;

    public UserRequest(String userId, String email, String password, GlobalRole globalRole) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.globalRole = globalRole;
//        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public GlobalRole getGlobalRole() {
        return globalRole;
    }

//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
}
