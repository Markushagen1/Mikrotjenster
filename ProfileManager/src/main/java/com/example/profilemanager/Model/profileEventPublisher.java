package com.example.profilemanager.Model;

import com.example.profilemanager.eventdriven.UserProfileDTO;
import com.example.profilemanager.mapper.UserProfileMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class profileEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final ObjectMapper objectMapper;

    @Autowired
    public profileEventPublisher(RabbitTemplate rabbitTemplate, @Value("${amqp.exchange.name}") String exchangeName, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.objectMapper = objectMapper;
    }

    public void publishProfileEvent(UserProfile userProfile) {
        UserProfileDTO userProfileDTO = UserProfileMapper.toDTO(userProfile);
        try {
            String event = objectMapper.writeValueAsString(userProfileDTO);
            rabbitTemplate.convertAndSend(exchangeName, "profile.created", event);
            log.info("Publiserte 'profile.created' hendelse for bruker: {}", userProfileDTO.getName());
        } catch (JsonProcessingException e) {
            log.error("Feil under serialisering av UserProfileDTO til JSON", e);
        }
    }
}





