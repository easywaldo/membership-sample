package com.membership.membershipsample;

import com.membership.membershipsample.common.SHAEncryptServiceImpl;
import com.membership.membershipsample.member.entity.Member;
import com.membership.membershipsample.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class MembershipSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MembershipSampleApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(
        MemberRepository memberRepository) {

        return (args) -> {
            memberRepository.deleteAll();
            memberRepository.save(Member.builder()
                .phoneNumber("1111")
                .email("acetious@gmail.com")
                .password(SHAEncryptServiceImpl.getSHA512("1111"))
                .name("이지남")
                .nickName("별명")
                .passwordValidation(true)
                .build());
        };
    }

}
