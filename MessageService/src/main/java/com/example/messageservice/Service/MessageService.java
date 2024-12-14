package com.example.messageservice.Service;

import com.example.messageservice.Model.Message;
import com.example.messageservice.Repo.MessageRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Message saveMessage(Message message) {
        return messageRepo.save(message);
    }

    public List<Message> getMessages(Long senderId, Long receiverId) {
        return messageRepo.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    public List<Message> getMessagesForReceiver(Long receiverId) {
        return messageRepo.findByReceiverId(receiverId);
    }
}


