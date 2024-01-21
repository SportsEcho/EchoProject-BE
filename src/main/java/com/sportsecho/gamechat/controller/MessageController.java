package com.sportsecho.gamechat.controller;

import com.sportsecho.gamechat.dto.GameChatRequestDto;
import com.sportsecho.gamechat.dto.GameChatResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@Slf4j(topic = "MessageController")
@RequiredArgsConstructor
public class MessageController {

    @MessageMapping("/gameChat/{gameId}")
    @SendTo("/topic/{gameId}")
    public GameChatResponseDto publish(
        @DestinationVariable("gameId") Long gameId,
        GameChatRequestDto request
    ) {
        return new GameChatResponseDto(HtmlUtils.htmlEscape(request.getMessage()));
    }
}
