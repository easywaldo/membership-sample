package com.membership.membershipsample.member.controller.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class JoinRequest {
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;
    @NotBlank(message = "별명을 입력해주세요.")
    private String nickName;
    @NotBlank(message = "휴대번호를 입력해주세요.")
    private String phoneNumber;
}
