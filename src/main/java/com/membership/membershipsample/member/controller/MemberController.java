package com.membership.membershipsample.member.controller;

import com.membership.membershipsample.common.SHAEncryptServiceImpl;
import com.membership.membershipsample.member.controller.request.JoinRequest;
import com.membership.membershipsample.member.controller.request.LoginRequest;
import com.membership.membershipsample.member.controller.request.PhoneCheckRequest;
import com.membership.membershipsample.member.controller.request.SendTokenRequest;
import com.membership.membershipsample.member.dto.JoinMemberDto;
import com.membership.membershipsample.member.service.MemberService;
import com.membership.membershipsample.message.dto.SendMessageDto;
import com.membership.membershipsample.message.entity.MessageType;
import com.membership.membershipsample.message.service.SmsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDateTime;

@RestController
public class MemberController {

    private final SmsService smsService;
    private final MemberService memberService;
    private final Validator validator;

    @Autowired
    public MemberController(SmsService smsService,
                            MemberService memberService) {

        this.memberService = memberService;
        this.smsService = smsService;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ApiOperation(value = "휴대폰번호 인증문자발송", notes = "")
    @PostMapping("/member/send-join-token")
    public Mono<ResponseEntity<?>> sendToken(@RequestBody @Validated SendTokenRequest request,
                                             @ApiIgnore Errors errors) {
        this.validator.validate(request);
        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().body(errors.getAllErrors()));
        }
        return Mono.just(ResponseEntity.accepted().body(this.smsService.sendMessage(SendMessageDto.builder()
            .message(this.smsService.issueToken())
            .phoneNumber(request.getPhoneNumber())
            .messageType(MessageType.JOIN_VALIDATION)
            .build())));
    }

    @ApiOperation(value = "휴대폰번호 인증절차", notes = "")
    @PostMapping("/member/phone-check-join-token")
    public Mono<ResponseEntity<?>> phoneCheck(@RequestBody @Validated PhoneCheckRequest request,
                                           @ApiIgnore Errors errors) {
        this.validator.validate(request);
        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().body(errors.getAllErrors()));
        }
        return Mono.just(ResponseEntity.accepted().body(
            this.smsService.isValidMessage(SendMessageDto.builder()
                .currentDate(LocalDateTime.now())
                .phoneNumber(request.getPhoneNumber())
                .message(request.getMessage())
                .build())));
    }

    @ApiOperation(value = "회원가입 진행완료", notes = "")
    @PostMapping("/member/join")
    public Mono<ResponseEntity<?>> memberJoin(@RequestBody @Validated JoinRequest request,
                                           @ApiIgnore Errors errors) {
        this.validator.validate(request);
        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().body(errors.getAllErrors()));
        }

        String password = SHAEncryptServiceImpl.getSHA512(request.getPassword());
        return Mono.just(ResponseEntity.accepted().body(this.memberService.joinMember(JoinMemberDto.builder()
            .email(request.getEmail())
            .name(request.getName())
            .nickName(request.getNickName())
            .phoneNumber(request.getPhoneNumber())
            .password(password)
            .build())));
    }

    @ApiOperation(value = "회원 로그인 진행", notes = "")
    @PostMapping("/member/login")
    public Mono<ResponseEntity<?>> memberLogin(@RequestBody LoginRequest request,
                                               @ApiIgnore Errors errors) {
        this.validator.validate(request);
        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().body(errors.getAllErrors()));
        }

        String password = SHAEncryptServiceImpl.getSHA512(request.getPassword());
        return Mono.just(ResponseEntity.accepted().body(this.memberService.memberLogin(JoinMemberDto.builder()
            .password(password)
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .build())));
    }

}
