package com.membership.membershipsample.message.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "is_send")
    private Boolean isSendMessage;

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

    public void updateSendFlag() {
        this.sendDate = LocalDateTime.now();
        this.isSendMessage = true;
    }
}
