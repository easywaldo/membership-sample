package com.membership.membershipsample.member.controller.request;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest implements Validator {
    private String email;
    private String phoneNumber;
    @NotBlank(message = "비밀번호는 반드시 입력해야 합니다.")
    private String password;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginRequest loginRequest = (LoginRequest) target;
        if (Strings.isNullOrEmpty(loginRequest.phoneNumber) || Strings.isNullOrEmpty(loginRequest.email)) {
            errors.reject("Parameters not input", "이메일 또는 휴대번호를 입력해주세요.");
        }
    }
}
