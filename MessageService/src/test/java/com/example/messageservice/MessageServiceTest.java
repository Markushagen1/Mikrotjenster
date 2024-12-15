package com.example.messageservice;


import com.example.messageservice.Model.Message;
import com.example.messageservice.Repo.MessageRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MessageServiceTest {

    @Autowired
    private MessageRepo messageRepo;

    @Test
    public void testSaveAndFetchMessage() {
        // Opprett en ny melding
        Message message = new Message();
        message.setSenderId(1L);
        message.setReceiverId(2L);
        message.setMessageContent("Test message via JUnit");
        message.setTimestamp(LocalDateTime.now());

        // Lagre meldingen
        Message savedMessage = messageRepo.save(message);

        // Bekreft at ID er generert
        assertThat(savedMessage.getId()).isNotNull();

        // Hent meldinger fra databasen
        List<Message> messages = messageRepo.findBySenderIdAndReceiverId(1L, 2L);

        // Bekreft at den lagrede meldingen finnes i databasen
        assertThat(messages).isNotEmpty();
        assertThat(messages.get(0).getMessageContent()).isEqualTo("Test message via JUnit");

        // Print meldingen for logging
        System.out.println("Saved message: " + savedMessage);
    }
}
