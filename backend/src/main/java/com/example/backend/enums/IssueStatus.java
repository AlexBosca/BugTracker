package com.example.backend.enums;

import com.example.backend.exception.issue.IssueStatusInvalidTransitionException;

import java.util.List;

import static java.util.List.of;

public enum IssueStatus {

    CLOSED("C", of()),
    VERIFIED("V", of(CLOSED)),
    RETEST("RT", of(VERIFIED)),
    PENDING_RETEST("PR", of(RETEST)),
    FIXED("F", of(PENDING_RETEST)),
    DUPLICATE("DU", of()),
    REJECTED("RJ", of()),
    DEFERRED("DF", of()),
    NOT_A_BUG("NAB", of()),
    OPEN("O", of(FIXED, DUPLICATE, REJECTED, DEFERRED, NOT_A_BUG)),
    ASSIGNED("A", of(OPEN)),
    NEW("N", of(ASSIGNED)),
    REOPENED("RO", of(OPEN));

    private final String code;
    private final List<IssueStatus> nextStates;

    IssueStatus(String code, List<IssueStatus> nextStates) {
        this.code = code;
        this.nextStates = nextStates;
    }

    public String getCode() {
        return code;
    }
    public IssueStatus getNextState(IssueStatus possibleNextState) {
        String currentState = name();

        return nextStates.stream()
                .filter(possibleNextState::equals)
                .findAny()
                .orElseThrow(() -> new IssueStatusInvalidTransitionException(currentState, possibleNextState.name()));
    }
}
