package com.sportsecho.common.kafka;

import com.sportsecho.common.mongo.MongoUtil;
import com.sportsecho.gamechat.dto.MessageRequestDto;
import com.sportsecho.gamechat.dto.MessageResponseDto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KafkaMessageUtil")
public class KafkaMessageUtil {

    private final KafkaTemplate<String, MessageResponseDto> kafkaTemplate;
    private final SimpMessagingTemplate template;

    private final MongoUtil mongoUtil;

    public void send(String topic, MessageRequestDto messageRequestDto) {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        String sendTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        MessageResponseDto messageResponseDto = MessageResponseDto.builder()
            .roomId(messageRequestDto.getRoomId())
            .sender(messageRequestDto.getSender())
            .message(messageRequestDto.getMessage())
            .sendTime(sendTime)
            .build();

        //여기서의 topic은 kafka broker의 topic을 의미함
        kafkaTemplate.send(topic, messageResponseDto);

        //mongoDB에 message 저장
        mongoUtil.saveMessage(messageResponseDto);
    }

    @KafkaListener(topics = KafkaConst.KAFKA_TOPIC)
    public void consume(MessageResponseDto messageResponseDto) {
        //destination을 구독하고있는 모든 사용자에게 message 전송
        template.convertAndSend("/topic/" + messageResponseDto.getRoomId(), messageResponseDto);
    }
}

