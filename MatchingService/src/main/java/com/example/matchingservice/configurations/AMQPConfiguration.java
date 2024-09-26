package com.example.matchingservice.configurations;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfiguration {
    @Bean
    public TopicExchange roundTopicExchange(
            @Value("${amqp.exchange.name}") final String exchangeName
    ) {
        // create the exchange object
        return ExchangeBuilder
                .topicExchange(exchangeName)
                .durable(true)
                .build();
    }
}
