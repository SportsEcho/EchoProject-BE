package com.sportsecho.common.configuration.kafka;

import com.google.common.collect.ImmutableMap;
import com.sportsecho.gamechat.dto.MessageResponseDto;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@EnableKafka
@Configuration
@Slf4j(topic = "KafkaProducerConfig")
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, MessageResponseDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfiguration());
    }

    @Bean
    public Map<String, Object> kafkaProducerConfiguration() {
        //Kafka Producer 구성을 위한 값 설정
        return ImmutableMap.<String, Object>builder()
            .put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER)
            .put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
            .put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
            .put("group.id", KafkaConst.GROUP_ID)
            .build();
    }

    @Bean
    public KafkaTemplate<String, MessageResponseDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
