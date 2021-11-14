package com.membership.membershipsample.member.controller;

import com.membership.membershipsample.common.SHAEncryptServiceImpl;
import com.membership.membershipsample.member.controller.request.*;
import com.membership.membershipsample.member.controller.response.LoginResultType;
import com.membership.membershipsample.member.dto.JoinMemberDto;
import com.membership.membershipsample.member.service.AuthService;
import com.membership.membershipsample.member.service.CookieService;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@RestController
public class MemberController {

    private final SmsService smsService;
    private final MemberService memberService;
    private final Validator validator;
    private final AuthService authService;

    @Autowired
    public MemberController(SmsService smsService,
                            MemberService memberService,
                            AuthService authService) {

        this.memberService = memberService;
        this.smsService = smsService;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.authService = authService;
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
                .messageType(MessageType.JOIN_VALIDATION)
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
    public Mono<ResponseEntity<?>> memberLogin(@RequestBody @Validated LoginRequest request,
                                               @ApiIgnore HttpServletResponse response,
                                               @ApiIgnore Errors errors) throws UnsupportedEncodingException {
        this.validator.validate(request);
        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().body(errors.getAllErrors()));
        }

        String password = SHAEncryptServiceImpl.getSHA512(request.getPassword());

        var loginResult = this.memberService.memberLogin(JoinMemberDto.builder()
            .password(password)
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .build());

        String userJwt = authService.issueToken(loginResult.getEmail());
        if (loginResult.getLoginResultType().equals(LoginResultType.SUCCESS)) {
            response.addCookie(CookieService.addCookie("userJwt", userJwt));
        }

        return Mono.just(ResponseEntity.accepted().body(loginResult));
    }

    @ApiOperation(value = "비밀번호 리셋요청 진행", notes = "비밀번호를 분실하였을 경우 비밀번호를 임시비밀번호로 초기화하며 비밀번호 인증여부를 미인증으로 변경한다")
    @PostMapping("/member/reset-password")
    public Mono<ResponseEntity<?>> resetPassword(@RequestBody @Validated ResetPasswordRequest request,
                                                 @ApiIgnore Errors errors) {
        this.validator.validate(request);
        if (errors.hasErrors()) {
            return Mono.just(ResponseEntity.badRequest().body(errors.getAllErrors()));
        }
        return Mono.just(ResponseEntity.accepted().body(this.memberService.memberResetPassword(JoinMemberDto.builder()
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .build())));
    }

    @ApiOperation(value = "내 정보보기", notes = "")
    @PostMapping("/member/me-info")
    public Mono<ResponseEntity<?>> selectMe(HttpServletRequest requestServlet) {
        String userJwt = authService.getUserIdFromJwtCookie(requestServlet);
        if (userJwt.equals("")) {
            return Mono.just(ResponseEntity.badRequest().body(JoinMemberDto.builder()
                .name("UnknownUser")
                .build()));
        }
        return Mono.just(ResponseEntity.accepted().body(this.memberService.selectMe(userJwt)));
    }

    @ApiOperation(value = "로그아웃", notes = "회원에 대한 로그아웃을 수행하여 jwt 토큰값을 삭제한다")
    @PostMapping("/member/logout")
    public Mono<ResponseEntity<?>> memberLogout(@ApiIgnore HttpServletResponse response) {
        Cookie deleteCookie = CookieService.deleteCookie("userJwt");
        response.addCookie(deleteCookie);

        return Mono.just(ResponseEntity.accepted().body(true));
    }
}
