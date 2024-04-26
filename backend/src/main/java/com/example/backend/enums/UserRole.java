package com.example.backend.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.backend.exception.user.UserRoleNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.backend.enums.UserPrivilege.*;

@AllArgsConstructor
@Getter
public enum UserRole implements GrantedAuthority {

    ROLE_ADMIN("A", Set.of(
            getAllPrivileges()
    )),
    ROLE_DEVELOPER("D", Set.of(
            ISSUE_CREATE, ISSUE_READ, ISSUE_UPDATE,
            PROJECT_READ,
            TEAM_READ,
            USER_READ,
            COMMENT_CREATE, COMMENT_READ, COMMENT_UPDATE, COMMENT_DELETE
    )),
    ROLE_TESTER("T", Set.of(
            ISSUE_CREATE, ISSUE_READ, ISSUE_UPDATE,
            PROJECT_READ,
            TEAM_READ,
            USER_READ,
            COMMENT_CREATE, COMMENT_READ, COMMENT_UPDATE, COMMENT_DELETE
    )),
    ROLE_SCRUM_MASTER("SM", Set.of(
            ISSUE_CREATE, ISSUE_READ, ISSUE_UPDATE,
            PROJECT_READ,
            TEAM_CREATE, TEAM_READ, TEAM_UPDATE,
            USER_READ,
            COMMENT_CREATE, COMMENT_READ, COMMENT_UPDATE, COMMENT_DELETE
    )),
    ROLE_PROJECT_MANAGER("PM", Set.of(
            ISSUE_CREATE, ISSUE_READ, ISSUE_UPDATE, // can't change issue's state, ex: SUBMITTED -> IN_WORK
            PROJECT_CREATE, PROJECT_READ, PROJECT_UPDATE,
            TEAM_READ,
            USER_READ,
            COMMENT_CREATE, COMMENT_READ, COMMENT_UPDATE, COMMENT_DELETE
    )),
    ROLE_VISITOR("V", Set.of(
            ISSUE_READ,
            PROJECT_READ,
            TEAM_READ,
            USER_READ,
            COMMENT_READ
    ));

    private final String code;
    private final Set<UserPrivilege> privileges;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

        return getPrivileges().stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.name()))
                .collect(Collectors.toSet());
    }
    
    @Override
    public String getAuthority() {
        return name();
    }

    public static Set<UserRole> getAllRoles() {
        return Set.of(values());
    }

    public static UserRole getRoleByCode(String code) {
        return getAllRoles().stream()
                .filter(role -> role.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new UserRoleNotFoundException(code));
    }

    public String getName() {
        return this.name().replace("ROLE_", "");
    }
}
