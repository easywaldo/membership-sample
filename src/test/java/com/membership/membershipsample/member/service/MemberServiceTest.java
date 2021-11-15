package com.membership.membershipsample.member.service;

import com.membership.membershipsample.member.dto.JoinMemberDto;
import com.membership.membershipsample.member.entity.Member;
import com.membership.membershipsample.member.repository.MemberRepository;
import com.membership.membershipsample.message.entity.Message;
import com.membership.membershipsample.message.repository.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    public MemberService memberService;
    @Autowired
    public MemberRepository memberRepository;
    @Autowired
    public MessageRepository messageRepository;

    @BeforeEach
    public void setUp() {
        this.memberRepository.deleteAll();
        this.messageRepository.deleteAll();
    }

    @Test
    public void 회원이_가입이_되는지_확인한다() {
        // arrange
        this.messageRepository.save(Message.builder()
            .phoneNumber("0001112222")
            .confirmDate(LocalDateTime.now())
            .build());
        JoinMemberDto joinMemberDto = JoinMemberDto.builder()
            .email("tester@gmail.com")
            .password("1234")
            .name("tester")
            .phoneNumber("0001112222")
            .nickName("별명")
            .build();

        // act
        var joinResult = this.memberService.joinMember(joinMemberDto);

        // assert
        var memberDto = this.memberRepository.findById(
            joinResult.getMemberSeq()).map(x ->
                JoinMemberDto.builder()
                    .name(x.getName())
                    .nickName(x.getNickName())
                    .phoneNumber(x.getPhoneNumber())
                    .email(x.getEmail())
                    .password(x.getPassword())
                    .build());
        assertEquals(joinMemberDto, memberDto.get());
    }

    @Test
    public void 회원_기입에_대한_중복가입을_방지한다() {
        // arrange
        JoinMemberDto joinMemberDto = JoinMemberDto.builder()
            .email("tester@gmail.com")
            .password("1234")
            .name("tester")
            .phoneNumber("0001112222")
            .nickName("별명")
            .build();
        this.memberService.joinMember(joinMemberDto);

        // act
        var joinResult = this.memberService.joinMember(joinMemberDto);

        // assert
        assertEquals(0, joinResult.getMemberSeq());
    }

    @Test
    public void 비밀번호_변경_요청에_대한_정상_변경_여부를_확인한다() {
        // arrange
        var memberSeq = memberRepository.save(Member.builder()
            .email("tester@gmail.com")
            .phoneNumber("password")
            .password("password")
            .build()).getMemberSeq();

        // act
        var result = memberService.memberResetPassword(JoinMemberDto.builder()
            .email("tester@gmail.com")
            .phoneNumber("password")
            .build());

        // assert
        var updated = memberRepository.findById(memberSeq);
        assertNotNull(updated);
        assertEquals(memberSeq, updated.get().getMemberSeq());
        assertNotEquals("password", updated.get().getPassword());

    }
}