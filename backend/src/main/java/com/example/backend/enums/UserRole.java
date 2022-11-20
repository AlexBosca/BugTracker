package com.example.backend.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.backend.enums.UserPrivilege.*;

public enum UserRole {

    ADMIN("A", Set.of(
            getAllPrivileges()
    )),
    DEVELOPER("D", Set.of(
            ISSUE_CREATE, ISSUE_READ, ISSUE_UPDATE,
            PROJECT_READ,
            TEAM_READ,
            USER_READ,
            COMMENT_CREATE, COMMENT_READ, COMMENT_UPDATE, COMMENT_DELETE
    )),
    TESTER("T", Set.of(
            ISSUE_CREATE, ISSUE_READ, ISSUE_UPDATE,
            PROJECT_READ,
            TEAM_READ,
            USER_READ,
            COMMENT_CREATE, COMMENT_READ, COMMENT_UPDATE, COMMENT_DELETE
    )),
    SCRUM_MASTER("SM", Set.of(
            ISSUE_CREATE, ISSUE_READ, ISSUE_UPDATE,
            PROJECT_READ,
            TEAM_CREATE, TEAM_READ, TEAM_UPDATE,
            USER_READ,
            COMMENT_CREATE, COMMENT_READ, COMMENT_UPDATE, COMMENT_DELETE
    )),
    PROJECT_MANAGER("PM", Set.of(
            ISSUE_CREATE, ISSUE_READ, ISSUE_UPDATE, // can't change issue's state, ex: SUBMITTED -> IN_WORK
            PROJECT_CREATE, PROJECT_READ, PROJECT_UPDATE,
            TEAM_CREATE, TEAM_READ, TEAM_UPDATE,
            USER_READ,
            COMMENT_CREATE, COMMENT_READ, COMMENT_UPDATE, COMMENT_DELETE
    )),
    VISITOR("V", Set.of(
            ISSUE_READ,
            PROJECT_READ,
            TEAM_READ,
            USER_READ,
            COMMENT_READ
    ));

    private final String code;
    private final Set<UserPrivilege> privileges;

    UserRole(String code, Set<UserPrivilege> privileges) {
        this.code = code;
        this.privileges = privileges;
    }

    public String getCode() {
        return code;
    }

    public Set<UserPrivilege> getPrivileges() {
        return privileges;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {

        return getPrivileges().stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.getCode()))
                .collect(Collectors.toSet());
    }
}
