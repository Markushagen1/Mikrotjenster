package com.example.messageservice.Controller;


import com.example.messageservice.Model.Message;
import com.example.messageservice.Service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.saveMessage(message));
    }

    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable Long senderId,
            @PathVariable Long receiverId) {
        return ResponseEntity.ok(messageService.getMessages(senderId, receiverId));
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<Message>> getMessagesForReceiver(@PathVariable Long receiverId) {
        return ResponseEntity.ok(messageService.getMessagesForReceiver(receiverId));
    }
}

