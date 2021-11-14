package com.membership.membershipsample.member.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class ResetPasswordRequest {
    @NotBlank(message = "이메일은 필수 입력 사항 입니다.")
    private String email;
    @NotBlank(message = "휴대폰번호는 필수 입력 사항입니다.")
    private String phoneNumber;
}
