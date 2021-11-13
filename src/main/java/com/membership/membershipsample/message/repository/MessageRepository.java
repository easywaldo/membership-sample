package com.membership.membershipsample.message.repository;

import com.membership.membershipsample.message.entity.Message;
import com.membership.membershipsample.message.entity.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByMessageTypeAndPhoneNumberAndMessageAndCreateDateAfter(MessageType messageType, String phoneNumber, String message, LocalDateTime createDate);
    List<Message> findByPhoneNumberAndConfirmDateAfter(String phoneNumber, LocalDateTime confirmDate);
}
