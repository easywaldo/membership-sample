package com.membership.membershipsample.message.dto;

import com.membership.membershipsample.message.entity.Message;
import com.membership.membershipsample.message.entity.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SendMessageDto {
    private String phoneNumber;
    private String message;
    private String SENDER_PHONE_NUMBER = "1588-1111";
    private LocalDateTime currentDate;
    private MessageType messageType;

    @Builder
    public SendMessageDto(String phoneNumber, String message, LocalDateTime currentDate, MessageType messageType) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.currentDate = currentDate;
        this.messageType = messageType;
    }

    public Message toEntity() {
        return Message.builder()
            .isSendMessage(false)
            .sendDate(LocalDateTime.now().plusSeconds(3L))
            .message(this.message)
            .expiredDate(LocalDateTime.now().plusMinutes(3L))
            .senderPhoneNumber(SENDER_PHONE_NUMBER)
            .phoneNumber(this.phoneNumber)
            .messageType(this.messageType)
            .build();
    }
}
