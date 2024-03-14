package com.sportsecho.common.configuration.kafka;

import com.google.common.collect.ImmutableMap;
import com.sportsecho.gamechat.dto.MessageResponseDto;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
@Slf4j(topic = "KafkaConsumerConfig")
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageResponseDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageResponseDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public ConsumerFactory<String, MessageResponseDto> consumerFactory() {

        JsonDeserializer<MessageResponseDto> deserializer = new JsonDeserializer<>();

        // 모든 패키지 신뢰
        deserializer.addTrustedPackages("*");

        // Kafka Consumer 구성을 위한 값 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
        Map<String, Object> config = ImmutableMap.<String, Object>builder()
            .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConst.KAFKA_BROKER)
            .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
            .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
            .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
            .put("group.id", KafkaConst.GROUP_ID)
            .build();

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
    }
}


