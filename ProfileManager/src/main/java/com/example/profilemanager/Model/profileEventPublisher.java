package com.example.profilemanager.Model;

import com.example.profilemanager.eventdriven.UserProfileDTO;
import com.example.profilemanager.mapper.UserProfileMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class profileEventPublisher {
    private final AmqpTemplate amqpTemplate;
    private final String exchangeName;
    private final ObjectMapper objectMapper;

    public profileEventPublisher(
            final AmqpTemplate amqpTemplate,
            @Value("${amqp.exchange.name}") final String exchangeName,
            final ObjectMapper objectMapper
    ) {
        this.amqpTemplate = amqpTemplate;
        this.exchangeName = exchangeName;
        this.objectMapper = objectMapper;
    }

    public void publishProfileEvent(UserProfile userProfile) {
        // Konverter UserProfile til UserProfileDTO
        UserProfileDTO userProfileDTO = UserProfileMapper.toDTO(userProfile);

        try {
            // Konverter DTO til JSON-streng ved bruk av ObjectMapper
            String event = objectMapper.writeValueAsString(userProfileDTO);

            // Send meldingen til RabbitMQ
            amqpTemplate.convertAndSend(exchangeName, "profile.created", event);

            // Legg til logging for å bekrefte at meldingen blir sendt
            log.info("Published 'profile.created' event for user: {} with message: {}", userProfileDTO.getName(), event);
        } catch (JsonProcessingException e) {
            log.error("Error while serializing UserProfileDTO to JSON", e);
        }
    }
}



