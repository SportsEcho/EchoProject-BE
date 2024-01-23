package com.sportsecho.gamechat.controller;

import com.sportsecho.gamechat.dto.GameChatRequestDto;
import com.sportsecho.gamechat.dto.GameChatResponseDto;
import java.text.SimpleDateFormat;
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
        // 현재 시간을 Date 객체로 얻기
        Date now = new Date();

        // SimpleDateFormat을 사용하여 형식 지정
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

        // 현재 시간을 지정한 형식으로 문자열로 변환
        String formattedTime = formatter.format(now);

        return GameChatResponseDto.builder()
            .sender(request.getSender())
            .content(request.getMessage())
            .sendAt(formattedTime)
            .build();
    }
}
