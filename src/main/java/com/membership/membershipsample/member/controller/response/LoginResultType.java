package com.membership.membershipsample.member.controller.response;

public enum LoginResultType {
    SUCCESS(0),
    NOT_MATCH(1);

    private int loginResultType;

    LoginResultType(int loginResultType) {
        this.loginResultType = loginResultType;
    }
}
