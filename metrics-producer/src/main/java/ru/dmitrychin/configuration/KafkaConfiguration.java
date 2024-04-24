package ru.dmitrychin.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import ru.dmitrychin.entity.DiskFreeMetrics;
import ru.dmitrychin.entity.HttpRequestsMetrics;

import java.util.HashMap;
import java.util.Map;

import static ru.dmitrychin.model.KafkaTopic.DISK_METRICS_TOPIC;
import static ru.dmitrychin.model.KafkaTopic.HTTP_METRICS_TOPIC;

@Configuration
public class KafkaConfiguration {
    @Bean
    public NewTopic httpTopic() {
        return new NewTopic(HTTP_METRICS_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic diskTopic() {
        return new NewTopic(DISK_METRICS_TOPIC, 1, (short) 1);
    }

    @Bean
    public RecordMessageConverter converter() {
        JsonMessageConverter converter = new StringJsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("ru.dmitrychin.entity");
        Map<String, Class<?>> classMap = new HashMap<>();
        classMap.put("disk", DiskFreeMetrics.class);
        classMap.put("http", HttpRequestsMetrics.class);
        typeMapper.setIdClassMapping(classMap);
        converter.setTypeMapper(typeMapper);
        return converter;
    }
}


