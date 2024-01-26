package com.sportsecho.gamechat.controller;

import com.sportsecho.gamechat.dto.GameChatRequestDto;
import com.sportsecho.gamechat.dto.GameChatResponseDto;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j(topic = "MessageController")
@RequiredArgsConstructor
public class MessageController {

    @MessageMapping("/gameChat/{gameId}/enter")
    @SendTo("/topic/{gameId}")
    public GameChatResponseDto enter(
        @DestinationVariable("gameId") Long gameId,
        GameChatRequestDto request
    ) {

        return GameChatResponseDto.builder()
            .content(request.getSender() + "님이 입장하셨습니다.")
            .build();
    }

    @MessageMapping("/gameChat/{gameId}")
    @SendTo("/topic/{gameId}")
    public GameChatResponseDto publish(
        @DestinationVariable("gameId") Long gameId,
        GameChatRequestDto request
    ) {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm"));

        return GameChatResponseDto.builder()
            .sender(request.getSender())
            .content(request.getMessage())
            .sendAt(formattedTime)
            .build();
    }
}
