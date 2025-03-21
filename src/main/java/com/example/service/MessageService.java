package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /*Create messages */
    public Optional<Message> createMessage(Message message) {
        // Validate the user exists
        Optional<Account> user = accountRepository.findById(message.getPostedBy());
        if (!user.isPresent()) {
            return Optional.empty();
        }

        // Save and return the message
        Message savedMessage = messageRepository.save(message);
        return Optional.of(savedMessage);
    }

    /*Retrieve All messages */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /*Retrieve Message by ID */
    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
    }

    /*Delete Message by ID */
    public int deleteMessageById(int messageId) {
        // Check if the message exists in the database
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId); 
            return 1; 
        }
        return 0; 
    }

    /*Update Message by ID */
    public int updateMessage(int messageId, String messageText) {
        // Check if the message exists in the database
        Optional<Message> existingMessageOpt = messageRepository.findById(messageId);
        if (existingMessageOpt.isPresent()) {
            Message existingMessage = existingMessageOpt.get();
            existingMessage.setMessageText(messageText);  
            messageRepository.save(existingMessage); 
            return 1;
        }
        return 0; 
    }

    /*Retrieve Messages by particular User */
    public List<Message> getMessagesByUserId(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }
}