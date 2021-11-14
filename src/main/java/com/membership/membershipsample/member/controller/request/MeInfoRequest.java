package com.membership.membershipsample.member.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class MeInfoRequest {
    @Min(1)
    private Long memberSeq;
}
