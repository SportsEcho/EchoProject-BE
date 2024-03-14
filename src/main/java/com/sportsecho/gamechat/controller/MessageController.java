package com.sportsecho.gamechat.controller;

import com.sportsecho.common.kafka.KafkaConst;
import com.sportsecho.common.kafka.KafkaMessageUtil;
import com.sportsecho.gamechat.dto.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "MessageController")
public class MessageController {

    private final KafkaMessageUtil kafkaMessageUtil;

    @MessageMapping("/message")
    public void sendMessage(MessageRequestDto messageRequestDto) {
        log.info("Received message\nsender = {}, message = {}", messageRequestDto.getSender(), messageRequestDto.getMessage());

        //message service logic
        kafkaMessageUtil.send(KafkaConst.KAFKA_TOPIC, messageRequestDto);
    }

}


