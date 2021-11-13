package com.membership.membershipsample.member.controller.response;

public enum JoinResultType {
    SUCCESS(0),
    ALREADY_JOINED(1),
    NOT_VALIDATED(2);

    private int joinResultType;

    JoinResultType(int joinResult) {
        this.joinResultType = joinResult;
    }
}
