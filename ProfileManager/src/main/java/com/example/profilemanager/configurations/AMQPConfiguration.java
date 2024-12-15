package com.example.profilemanager.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AMQPConfiguration {

    // Oppretter en TopicExchange Bean for bruk i profilopprettelse, likes, matches, etc.
    @Bean
    public TopicExchange profileExchange(
            @Value("${amqp.exchange.name}") final String exchangeName
    ) {
        return ExchangeBuilder
                .topicExchange(exchangeName)
                .durable(true)
                .build();
    }

    // Oppretter en Queue for profilopprettelse
    @Bean
    public Queue profileQueue(
            @Value("${amqp.queue.profile.name}") final String queueName
    ) {
        return QueueBuilder
                .durable(queueName)
                .build();
    }

    // Binding mellom profilQueue og exchange med routingKey for profilopprettelse
    @Bean
    public Binding profileBinding(Queue profileQueue, TopicExchange profileExchange,
                                  @Value("${amqp.routing.profile.key}") String routingKey) {
        return BindingBuilder
                .bind(profileQueue)
                .to(profileExchange)
                .with(routingKey);
    }

    // Oppretter en Queue for like-hendelser
    @Bean
    public Queue likeQueue(
            @Value("${amqp.queue.like.name}") final String queueName
    ) {
        return QueueBuilder
                .durable(queueName)
                .build();
    }

    // Binding mellom likeQueue og exchange med routingKey for likes
    @Bean
    public Binding likeBinding(Queue likeQueue, TopicExchange profileExchange,
                               @Value("${amqp.routing.like.key}") String routingKey) {
        return BindingBuilder
                .bind(likeQueue)
                .to(profileExchange)
                .with(routingKey);
    }

    // Oppretter en Queue for match-hendelser
    @Bean
    public Queue matchQueue(
            @Value("${amqp.queue.match.name}") final String queueName
    ) {
        return QueueBuilder
                .durable(queueName)
                .build();
    }

    // Binding mellom matchQueue og exchange med routingKey for matches
    @Bean
    public Binding matchBinding(Queue matchQueue, TopicExchange profileExchange,
                                @Value("${amqp.routing.match.key}") String routingKey) {
        return BindingBuilder
                .bind(matchQueue)
                .to(profileExchange)
                .with(routingKey);
    }

    // Konfigurerer RabbitTemplate med Jackson2MessageConverter for JSON
    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter jackson2MessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter);
        return rabbitTemplate;
    }

    // JSON-konvertering for RabbitMQ-meldinger
    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}





