package com.membership.membershipsample.message.dto;

import com.membership.membershipsample.message.entity.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SendMessageDto {
    private String phoneNumber;
    private String message;
    private final String SENDER_PHONE_NUMBER = "1588-1111";
    private LocalDateTime currentDate;

    @Builder
    public SendMessageDto(String phoneNumber, String message, LocalDateTime currentDate) {
        this.phoneNumber = phoneNumber;
        this.message = message;
        this.currentDate = currentDate;
    }

    public Message toEntity() {
        return Message.builder()
            .isSendMessage(false)
            .sendDate(LocalDateTime.now().plusSeconds(3L))
            .message(this.message)
            .expiredDate(LocalDateTime.now().plusMinutes(3L))
            .senderPhoneNumber(SENDER_PHONE_NUMBER)
            .phoneNumber(this.phoneNumber)
            .build();
    }
}
