package com.example.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserPrivilege {

    ISSUE_CREATE("IC"), ISSUE_READ("IR"), ISSUE_UPDATE("IU"), ISSUE_DELETE("ID"),
    PROJECT_CREATE("PC"), PROJECT_READ("PR"), PROJECT_UPDATE("PU"), PROJECT_DELETE("PD"),
    TEAM_CREATE("TC"), TEAM_READ("TR"), TEAM_UPDATE("TU"), TEAM_DELETE("TD"),
    USER_CREATE("UC"), USER_READ("UR"), USER_UPDATE("UU"), USER_DELETE("UD"),
    COMMENT_CREATE("CC"), COMMENT_READ("CR"), COMMENT_UPDATE("CU"), COMMENT_DELETE("CD");

    private final String code;

    public static UserPrivilege[] getAllPrivileges() {
        return values();
    }
}
