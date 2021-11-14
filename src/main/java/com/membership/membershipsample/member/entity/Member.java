package com.membership.membershipsample.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "member")
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_seq")
    private Long memberSeq;
    @Column(name = "email")
    private String email;
    @Column(name = "nickname")
    private String nickName;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password_validation")
    private Boolean passwordValidation;

    @Builder
    public Member(String email,
                  String nickName,
                  String password,
                  String name,
                  String phoneNumber,
                  Boolean passwordValidation) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.passwordValidation = passwordValidation;
    }

    public Member resetPassword(String resetToken) {
        this.password =  resetToken;
        this.passwordValidation = false;
        return this;
    }
}
