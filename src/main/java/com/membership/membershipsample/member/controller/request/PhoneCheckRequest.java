package com.membership.membershipsample.member.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PhoneCheckRequest {
    @NotBlank
    private String message;
}
