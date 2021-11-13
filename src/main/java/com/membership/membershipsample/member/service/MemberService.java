package com.membership.membershipsample.member.service;

import com.membership.membershipsample.member.controller.response.JoinResult;
import com.membership.membershipsample.member.controller.response.JoinResultType;
import com.membership.membershipsample.member.controller.response.LoginResultType;
import com.membership.membershipsample.member.dto.JoinMemberDto;
import com.membership.membershipsample.member.entity.Member;
import com.membership.membershipsample.member.repository.MemberRepository;
import com.membership.membershipsample.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         MessageRepository messageRepository) {
        this.memberRepository = memberRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public JoinResult joinMember(JoinMemberDto joinMemberDto) {
        // TODO: 회원 중복검사 등 데이터베이스 유효성 검사를 수행한다
        if (isDuplicatedMember(joinMemberDto)) {
            return JoinResult.builder()
                .memberSeq(0L)
                .joinResultType(JoinResultType.ALREADY_JOINED)
                .build();
        }

        if (messageRepository.findByPhoneNumberAndConfirmDateAfter(
            joinMemberDto.getPhoneNumber(), LocalDateTime.now().minusMinutes(3L)).size() == 0) {
            return JoinResult.builder()
                .memberSeq(0L)
                .joinResultType(JoinResultType.NOT_VALIDATED)
                .build();
        }

        Member joinedMember = memberRepository.save(joinMemberDto.toEntity());
        return JoinResult.builder()
            .memberSeq(joinedMember.getMemberSeq())
            .joinResultType(JoinResultType.SUCCESS)
            .build();
    }

    public boolean isDuplicatedMember(JoinMemberDto joinMemberDto) {
         return memberRepository.findMemberByPhoneNumberOrEmail(
             joinMemberDto.getPhoneNumber(), joinMemberDto.getEmail()).isPresent();
    }

    @Transactional(readOnly = true)
    public LoginResultType memberLogin(JoinMemberDto joinMemberDto) {
        var member = memberRepository.findMemberByPhoneNumberOrEmail(
            joinMemberDto.getPhoneNumber(), joinMemberDto.getEmail());
        if (member.isEmpty()) {
            return LoginResultType.NOT_MATCH;
        }
        if (!member.get().getPassword().equals(joinMemberDto.getPassword())) {
            return LoginResultType.NOT_MATCH;
        }
        return LoginResultType.SUCCESS;
    }

}
