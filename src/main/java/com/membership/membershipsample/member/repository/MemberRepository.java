package com.membership.membershipsample.member.repository;

import com.membership.membershipsample.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByPhoneNumberOrEmail(String phoneNumber, String email);
    Optional<Member> findMemberByEmail(String email);
}
