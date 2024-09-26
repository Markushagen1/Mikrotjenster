package com.example.profilemanager.configurations;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSONConfiguration {

    // No need for a Hibernate-specific module; just use the default Jackson ObjectMapper
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}



