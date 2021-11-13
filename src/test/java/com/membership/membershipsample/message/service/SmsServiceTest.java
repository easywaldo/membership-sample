package com.membership.membershipsample.message.service;

import com.membership.membershipsample.message.dto.SendMessageDto;
import com.membership.membershipsample.message.entity.MessageType;
import com.membership.membershipsample.message.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SmsServiceTest {

    @Autowired
    public SmsService smsService;
    @Autowired
    public MessageRepository messageRepository;

    @Test
    @Transactional
    public void SMS_발송하여_해당_시간내에_인증요청을_하는_경우_정상_유효성_확인을_할_수_있도록_한다() {
        // arrange
        var messageSeq = this.smsService.sendMessage(SendMessageDto.builder()
            .phoneNumber("1111")
            .message("TEST1234@@")
            .messageType(MessageType.JOIN_VALIDATION)
            .build());
        this.messageRepository.findById(messageSeq).get().updateSendFlag();


        // act
        var result = this.smsService.isValidMessage(SendMessageDto.builder()
            .phoneNumber("1111")
            .message("TEST1234@@")
            .messageType(MessageType.JOIN_VALIDATION)
            .currentDate(LocalDateTime.now().plusSeconds(10L))
            .build());

        // assert
        assertEquals(true, result);
    }

    @Test
    @Transactional
    public void SMS_발송하여_해당_시간을_초과하여_인증요청을_하는_경우_정상_유효성_확인을_할_수_있도록_한다() {
        // arrange
        var messageSeq = this.smsService.sendMessage(SendMessageDto.builder()
            .phoneNumber("1111")
            .message("TEST1234@@")
            .build());
        this.messageRepository.findById(messageSeq).get().updateSendFlag();


        // act
        var result = this.smsService.isValidMessage(SendMessageDto.builder()
            .phoneNumber("1111")
            .message("TEST1234@@")
            .currentDate(LocalDateTime.now().plusMinutes(10L))
            .build());

        // assert
        assertEquals(false, result);
    }

}