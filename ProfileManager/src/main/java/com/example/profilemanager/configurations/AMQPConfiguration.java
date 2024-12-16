package com.example.profilemanager.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AMQPConfiguration {

    @Bean
    public TopicExchange profileExchange(
            @Value("${amqp.exchange.name}") final String exchangeName
    ) {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Queue profileQueue(@Value("${amqp.queue.profile.name}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue likeQueue(@Value("${amqp.queue.like.name}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Queue matchQueue(@Value("${amqp.queue.match.name}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding profileBinding(Queue profileQueue, TopicExchange profileExchange,
                                  @Value("${amqp.routing.profile.key}") String routingKey) {
        return BindingBuilder.bind(profileQueue).to(profileExchange).with(routingKey);
    }

    @Bean
    public Binding likeBinding(Queue likeQueue, TopicExchange profileExchange,
                               @Value("${amqp.routing.like.key}") String routingKey) {
        return BindingBuilder.bind(likeQueue).to(profileExchange).with(routingKey);
    }

    @Bean
    public Binding matchBinding(Queue matchQueue, TopicExchange profileExchange,
                                @Value("${amqp.routing.match.key}") String routingKey) {
        return BindingBuilder.bind(matchQueue).to(profileExchange).with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationRunner adminRunner(RabbitAdmin rabbitAdmin) {
        return args -> rabbitAdmin.initialize();
    }
}




