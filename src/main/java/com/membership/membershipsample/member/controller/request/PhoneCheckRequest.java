package com.membership.membershipsample.member.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PhoneCheckRequest {
    @NotBlank(message = "회원가입절차에서 수신받은 메시지를 입력해주세요.")
    private String message;
    @NotBlank(message = "회원가입절차에 인증 메시지를 수신받은 휴대전화 번호를 입력해주세요.")
    private String phoneNumber;
}
