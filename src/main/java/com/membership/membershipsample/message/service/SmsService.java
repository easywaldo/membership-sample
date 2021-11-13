package com.membership.membershipsample.message.service;

import com.membership.membershipsample.message.dto.SendMessageDto;
import com.membership.membershipsample.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SmsService {

    private final MessageRepository messageRepository;

    @Autowired
    public SmsService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public Long sendMessage(SendMessageDto sendMessageDto) {
        return this.messageRepository.save(sendMessageDto.toEntity()).getMessageSeq();
    }

    @Transactional
    public boolean isValidMessage(SendMessageDto sendMessageDto) {
        var result = this.messageRepository.findByMessage(sendMessageDto.getMessage());
        if (result.isEmpty()) return false;
        var message = result.get();
        return message.getIsSendMessage().equals(true) &&
            message.getExpiredDate().isAfter(sendMessageDto.getCurrentDate()) &&
            message.getMessage().equals(sendMessageDto.getMessage());
    }
}
