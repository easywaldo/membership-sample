package com.membership.membershipsample.member.service;

import com.membership.membershipsample.common.SHAEncryptServiceImpl;
import com.membership.membershipsample.member.controller.request.LoginRequest;
import com.membership.membershipsample.member.controller.response.JoinResult;
import com.membership.membershipsample.member.controller.response.JoinResultType;
import com.membership.membershipsample.member.controller.response.LoginResultType;
import com.membership.membershipsample.member.dto.JoinMemberDto;
import com.membership.membershipsample.member.entity.Member;
import com.membership.membershipsample.member.repository.MemberRepository;
import com.membership.membershipsample.message.dto.SendMessageDto;
import com.membership.membershipsample.message.entity.MessageType;
import com.membership.membershipsample.message.repository.MessageRepository;
import com.membership.membershipsample.message.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final SmsService smsService;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         MessageRepository messageRepository,
                         SmsService smsService) {
        this.memberRepository = memberRepository;
        this.messageRepository = messageRepository;
        this.smsService = smsService;
    }

    @Transactional
    public JoinResult joinMember(JoinMemberDto joinMemberDto) {
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

    @Transactional
    public LoginResultType memberResetPassword(JoinMemberDto joinMemberDto) {
        var member = memberRepository.findMemberByPhoneNumberOrEmail(
            joinMemberDto.getPhoneNumber(), joinMemberDto.getEmail());

        if (member.isEmpty()) {
            return LoginResultType.NOT_MATCH;
        }

        var resetToken = smsService.issueToken();
        String password = SHAEncryptServiceImpl.getSHA512(resetToken);
        member.get().resetPassword(password);
        smsService.sendMessage(SendMessageDto.builder()
            .messageType(MessageType.PASSWORD_MODIFICATION)
            .phoneNumber(joinMemberDto.getPhoneNumber())
            .currentDate(LocalDateTime.now())
            .message(resetToken)
            .build());

        return LoginResultType.SUCCESS;
    }

    @Transactional(readOnly = true)
    public JoinMemberDto selectMe(Long memberSeq) {
        var member = memberRepository.findById(memberSeq);
        if (member.isEmpty()) {
            JoinMemberDto.builder()
                .memberSeq(0L)
                .name("UnknownUser")
                .build();
        }
        return JoinMemberDto.builder()
            .memberSeq(member.get().getMemberSeq())
            .email(member.get().getEmail())
            .nickName(member.get().getNickName())
            .phoneNumber(member.get().getPhoneNumber())
            .build();
    }

}
