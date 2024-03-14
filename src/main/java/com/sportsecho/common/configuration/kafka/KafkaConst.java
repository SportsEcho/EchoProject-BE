package com.sportsecho.common.configuration.kafka;

import java.util.UUID;

public interface KafkaConst {
    String GROUP_ID = UUID.randomUUID().toString();
    String KAFKA_TOPIC = "chat";
    String KAFKA_BROKER = "localhost:9092";
}
