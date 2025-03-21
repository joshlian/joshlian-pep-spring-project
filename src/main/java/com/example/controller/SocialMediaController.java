package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class SocialMediaController {
    private final AccountService accountService;

    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        Optional<Account> createdAccount = accountService.registerAccount(account.getUsername(), account.getPassword());

        if (!createdAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Return 409 
        }

        return ResponseEntity.ok(createdAccount.get());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Account account) {
        Optional<Account> loginedUser = accountService.loginUser(account.getUsername(), account.getPassword());
        if (!loginedUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 
        }
    
        return ResponseEntity.ok(loginedUser.get()); // 200 
    }

    /*Create messages */
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty() || message.getMessageText().length() > 255) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Message> createdMessage = messageService.createMessage(message);
        return createdMessage.map(ResponseEntity::ok)
                             .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /*Retrieve All messages */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        return messageService.getMessageById(messageId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(null));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<String> deleteMessageById(@PathVariable int messageId) {
        int rowsAffected = messageService.deleteMessageById(messageId);
        if (rowsAffected > 0) {
            return ResponseEntity.ok(String.valueOf(rowsAffected)); 
        }
        return ResponseEntity.ok("");  
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<String> updateMessage(@PathVariable int messageId, @RequestBody Map<String, String> requestBody) {
        String messageText = requestBody.get("messageText");
        
        // Validate the messageText
        if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
        }
        
        int rowsAffected = messageService.updateMessage(messageId, messageText);
        if (rowsAffected > 0) {
            return ResponseEntity.ok(String.valueOf(rowsAffected)); 
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
        }
    }
    
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByUser(@PathVariable Integer accountId) {
        return messageService.getMessagesByUserId(accountId);
    }
}
