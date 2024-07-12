package org.example.cvmaker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Register custom LocalDate deserializer
        SimpleModule customModule = new SimpleModule();
        customModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(customModule);

        return objectMapper;
    }
}
