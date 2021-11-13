package com.membership.membershipsample.message.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_seq")
    private Long messageSeq;

    @Column(name = "message")
    private String message;

    @Column(name = "receive_phone_number")
    private String phoneNumber;

    @Column(name = "sender_phone_number")
    private String senderPhoneNumber;

    @Column(name = "send_date")
    private LocalDateTime sendDate;

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "confirm_date")
    private LocalDateTime confirmDate;

    @Column(name = "is_send")
    private Boolean isSendMessage;

    @Column(name = "is_valid_member")
    private Boolean isValidMember;

    @Builder
    public Message(Long messageSeq,
                   String message,
                   String phoneNumber,
                   String senderPhoneNumber,
                   LocalDateTime expiredDate,
                   LocalDateTime sendDate,
                   LocalDateTime createDate,
                   Boolean isSendMessage) {
        this.messageSeq = messageSeq;
        this.message = message;
        this.phoneNumber = phoneNumber;
        this.senderPhoneNumber = senderPhoneNumber;
        this.expiredDate = expiredDate;
        this.sendDate = sendDate;
        this.createDate = createDate;
        this.isSendMessage = isSendMessage;
    }

    /**
     * 발송 스케줄러에 의해서 메시지 발송이 될 때 플래깅 처리
     */
    public void updateSendFlag() {
        this.sendDate = LocalDateTime.now();
        this.isSendMessage = true;
    }

    /***
     * 발송 문자확인이 되었을 경우 인증확인 플래깅 처리
     */
    public void validMemberFlag() {
        this.confirmDate = LocalDateTime.now();
        this.isValidMember = true;
    }
}
