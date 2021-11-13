package com.membership.membershipsample.member.service;

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
    public Long joinMember(JoinMemberDto joinMemberDto) {
        // TODO: 회원 중복검사 등 데이터베이스 유효성 검사를 수행한다
        if (isDuplicatedMember(joinMemberDto)) {
            return -100L;
        }

        if (messageRepository.findByPhoneNumberAndConfirmDateAfter(
            joinMemberDto.getPhoneNumber(), LocalDateTime.now().minusMinutes(3L)).size() == 0) {
            return -200L;
        }

        Member joinedMember = memberRepository.save(joinMemberDto.toEntity());
        return joinedMember.getMemberSeq();
    }

    public boolean isDuplicatedMember(JoinMemberDto joinMemberDto) {
         return memberRepository.findMemberByPhoneNumberOrEmail(
             joinMemberDto.getPhoneNumber(), joinMemberDto.getEmail()).isPresent();
    }

    @Transactional(readOnly = true)
    public JoinMemberDto memberLogin(JoinMemberDto joinMemberDto) {
        var member = memberRepository.findMemberByPhoneNumberOrEmail(
            joinMemberDto.getPhoneNumber(), joinMemberDto.getEmail()).orElseGet(() -> {
                return Member.builder()
                    .name("UnknownUser")
                    .nickName("UnknownUser")
                    .password("")
                    .build();
        });
        if (!member.getPassword().equals(joinMemberDto.getPassword())) {
            return JoinMemberDto.builder()
                .name("UnknownUser")
                .nickName("UnknownUser")
                .build();
        }
        return JoinMemberDto.builder()
            .email(member.getEmail())
            .name(member.getName())
            .nickName(member.getNickName())
            .phoneNumber(member.getPhoneNumber())
            .build();
    }

}
