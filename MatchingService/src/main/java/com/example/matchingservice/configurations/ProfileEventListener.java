package com.example.matchingservice.configurations;

import com.example.matchingservice.DTO.UserProfileDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProfileEventListener {

    @RabbitListener(queues = "profileQueue")
    public void handleProfileCreated(UserProfileDTO userProfileDTO) {
        // Logic for processing the event
        System.out.println("Received profile event for user: " + userProfileDTO.getName());
        // Matching logic goes here
    }
}

