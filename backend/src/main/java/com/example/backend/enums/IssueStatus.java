package com.example.backend.enums;

import com.example.backend.exception.issue.IssueStatusInvalidTransitionException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public enum IssueStatus {

    CLOSED("C"),
    VERIFIED("V"),
    RETEST("RT"),
    PENDING_RETEST("PR"),
    FIXED("F"),
    DUPLICATE("DU"),
    REJECTED("RJ"),
    DEFERRED("DF"),
    NOT_A_BUG("NAB"),
    OPEN("O"),
    ASSIGNED("A"),
    NEW("N"),
    REOPENED("RO");

    static {
        CLOSED.allowTransitionTo(REOPENED);
        VERIFIED.allowTransitionTo(CLOSED);
        RETEST.allowTransitionTo(VERIFIED, REOPENED);
        PENDING_RETEST.allowTransitionTo(RETEST);
        FIXED.allowTransitionTo(PENDING_RETEST);
        DUPLICATE.allowTransitionTo();
        REJECTED.allowTransitionTo();
        DEFERRED.allowTransitionTo();
        NOT_A_BUG.allowTransitionTo();
        OPEN.allowTransitionTo(FIXED, DUPLICATE, REJECTED, DEFERRED, NOT_A_BUG);
        ASSIGNED.allowTransitionTo(OPEN);
        NEW.allowTransitionTo(ASSIGNED);
        REOPENED.allowTransitionTo(OPEN);
    }

    private final String code;
    private IssueStatus[] possibleTransitions;

    public IssueStatus transitionTo(IssueStatus newState) {
        if(!canTransitionTo(newState)) {
            throw new IssueStatusInvalidTransitionException(this.name(), newState.name());
        }

        return newState;
    }

    public boolean canTransitionTo(IssueStatus anotherState) {
        return stream(possibleTransitions).anyMatch(anotherState::equals);
    }

    private void allowTransitionTo(IssueStatus... allowableStates) {
        possibleTransitions = allowableStates;
    }
}
