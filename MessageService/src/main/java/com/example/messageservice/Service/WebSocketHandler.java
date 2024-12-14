package com.example.messageservice.Service;

import com.example.messageservice.Model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final MessageService messageService;
    private final ObjectMapper objectMapper;
    private final Map<Long, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketHandler(MessageService messageService) {
        this.messageService = messageService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, String> parameters = getQueryParams(session.getUri().getQuery());
        Long userId = Long.parseLong(parameters.get("userId"));

        activeSessions.put(userId, session);
        session.sendMessage(new TextMessage("Connected as user " + userId));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // Parse innkommende melding
            Message incomingMessage = objectMapper.readValue(message.getPayload(), Message.class);
            Message savedMessage = messageService.saveMessage(incomingMessage);

            // Send melding til mottaker hvis vedkommende er online
            WebSocketSession receiverSession = activeSessions.get(savedMessage.getReceiverId());
            if (receiverSession != null && receiverSession.isOpen()) {
                String response = objectMapper.writeValueAsString(savedMessage);
                receiverSession.sendMessage(new TextMessage(response));
            }

            // Send bekreftelse til avsender
            String confirmation = objectMapper.writeValueAsString(savedMessage);
            session.sendMessage(new TextMessage(confirmation));

        } catch (Exception e) {
            e.printStackTrace();
            session.sendMessage(new TextMessage("{\"error\": \"Failed to process message\"}"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        activeSessions.values().remove(session);
    }

    private Map<String, String> getQueryParams(String query) {
        return Arrays.stream(query.split("&"))
                .map(param -> param.split("="))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }
}






