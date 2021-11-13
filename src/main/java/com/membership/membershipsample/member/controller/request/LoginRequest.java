package com.membership.membershipsample.member.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String phoneNumber;
    @NotBlank
    private String password;
}
