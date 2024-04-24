package ru.dmitrychin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
public class KafkaConfiguration {
    @Bean
    public RecordMessageConverter converter() {
        return new JsonMessageConverter();
    }
}
