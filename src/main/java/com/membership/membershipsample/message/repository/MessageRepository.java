package com.membership.membershipsample.message.repository;

import com.membership.membershipsample.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findByMessage(String message);
}
