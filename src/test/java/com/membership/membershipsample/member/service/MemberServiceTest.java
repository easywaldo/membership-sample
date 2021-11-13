package com.membership.membershipsample.member.service;

import com.membership.membershipsample.member.dto.JoinMemberDto;
import com.membership.membershipsample.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    public MemberService memberService;
    @Autowired
    public MemberRepository memberRepository;

    @Test
    public void 회원이_가입이_되는지_확인한다() {
        // arrange
        JoinMemberDto joinMemberDto = JoinMemberDto.builder()
            .email("tester@gmail.com")
            .password("1234")
            .name("tester")
            .phoneNumber("0001112222")
            .nickName("별명")
            .build();

        // act
        var resultSeqNo = this.memberService.joinMember(joinMemberDto);

        // assert
        var memberDto = this.memberRepository.findById(resultSeqNo).map(x ->
                JoinMemberDto.builder()
                    .name(x.getName())
                    .nickName(x.getNickName())
                    .phoneNumber(x.getPhoneNumber())
                    .email(x.getEmail())
                    .password(x.getPassword())
                    .build());
        assertEquals(joinMemberDto, memberDto.get());
    }
}