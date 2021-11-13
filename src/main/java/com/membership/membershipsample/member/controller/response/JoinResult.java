package com.membership.membershipsample.member.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinResult {
    private JoinResultType joinResultType;
    private Long memberSeq;

    @Builder
    public JoinResult(JoinResultType joinResultType, Long memberSeq) {
        this.joinResultType = joinResultType;
        this.memberSeq = memberSeq;
    }
}
