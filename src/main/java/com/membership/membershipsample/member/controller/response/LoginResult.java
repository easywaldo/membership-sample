package com.membership.membershipsample.member.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResult {
    private LoginResultType loginResultType;
    private String email;
    private String phoneNumber;

    @Builder
    public LoginResult(LoginResultType loginResultType,
                       String email,
                       String phoneNumber) {
        this.loginResultType = loginResultType;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
