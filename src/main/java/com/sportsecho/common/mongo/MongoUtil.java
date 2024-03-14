package com.sportsecho.common.mongo;

import com.sportsecho.common.kafka.KafkaConst;
import com.sportsecho.gamechat.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MongoUtil {

    private final MongoTemplate mongoTemplate;

    public void saveMessage(MessageResponseDto messageResponseDto) {
        mongoTemplate.insert(messageResponseDto, KafkaConst.KAFKA_TOPIC);
    }
}
