package com.membership.membershipsample.member.service;

import com.membership.membershipsample.member.dto.JoinMemberDto;
import com.membership.membershipsample.member.entity.Member;
import com.membership.membershipsample.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long joinMember(JoinMemberDto joinMemberDto) {
        // TODO: 회원 중복검사 등 데이터베이스 유효성 검사를 수행한다
        if (isDuplicatedMember(joinMemberDto)) {
            return 0L;
        }

        Member joinedMember = memberRepository.save(joinMemberDto.toEntity());
        return joinedMember.getMemberSeq();
    }

    public boolean isDuplicatedMember(JoinMemberDto joinMemberDto) {
         return memberRepository.findMemberByPhoneNumber(joinMemberDto.getPhoneNumber()).isPresent();
    }
}
