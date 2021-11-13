package com.membership.membershipsample.message.service;

import com.membership.membershipsample.message.dto.SendMessageDto;
import com.membership.membershipsample.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class SmsService {

    private final MessageRepository messageRepository;
    private static final String prefixValue = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random random = new Random();

    @Autowired
    public SmsService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public String issueToken() {
        char prefixA =  prefixValue.charAt(random.nextInt(prefixValue.length()));
        char prefixB =  prefixValue.charAt(random.nextInt(prefixValue.length()));
        char prefixC =  prefixValue.charAt(random.nextInt(prefixValue.length()));
        char prefixD =  prefixValue.charAt(random.nextInt(prefixValue.length()));
        char prefixE =  prefixValue.charAt(random.nextInt(prefixValue.length()));
        char prefixF =  prefixValue.charAt(random.nextInt(prefixValue.length()));
        char prefixG =  prefixValue.charAt(random.nextInt(prefixValue.length()));
        return String.format("%s%s%s%s%s%s%s%s%s%s",
            prefixA,
            random.nextInt(9),
            prefixB,
            random.nextInt(9),
            prefixC,
            random.nextInt(9),
            prefixD,
            prefixE,
            prefixF,
            prefixG);
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
        return message.getExpiredDate().isAfter(sendMessageDto.getCurrentDate()) &&
            message.getMessage().equals(sendMessageDto.getMessage());
    }
}
