package com.membership.membershipsample.member.dto;

import com.membership.membershipsample.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JoinMemberDto {
    private Long memberSeq;
    private String email;
    private String nickName;
    private String password;
    private String name;
    private String phoneNumber;

    @Builder
    public JoinMemberDto(String email,
                         String nickName,
                         String password,
                         String name,
                         String phoneNumber,
                         Long memberSeq) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.memberSeq = memberSeq;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        JoinMemberDto other = (JoinMemberDto)o;
        return this.email.equals(other.email) &&
            this.phoneNumber.equals(other.phoneNumber);
    }

    public Member toEntity() {
        return Member.builder()
            .email(this.email)
            .name(this.name)
            .nickName(this.nickName)
            .password(this.password)
            .phoneNumber(this.phoneNumber)
            .passwordValidation(true)
            .build();
    }
}
