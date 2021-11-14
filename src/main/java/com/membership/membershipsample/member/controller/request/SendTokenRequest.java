package com.membership.membershipsample.member.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SendTokenRequest {
    @NotBlank(message = "휴대전화 본인여부를 확인하기 위해 발송될 메시지 수신을 위한 휴대전화 번호를 입력해주세요.")
    private String phoneNumber;
}
