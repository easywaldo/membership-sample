package com.membership.membershipsample.member.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String phoneNumber;
    @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
    private String password;
}
