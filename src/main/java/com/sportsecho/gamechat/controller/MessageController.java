package com.sportsecho.gamechat.controller;


import com.sportsecho.gamechat.dto.MessageRequestDto;
import com.sportsecho.gamechat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final RoomService roomService;

    @MessageMapping("/chat/connect")
    public void connect(MessageRequestDto messageRequestDto) {
        roomService.connect(
            messageRequestDto.getType(),
            messageRequestDto.getRoomId(),
            messageRequestDto.getUserId()
        );
    }

    @MessageMapping("/chat/disconnect")
    public void disconnect(MessageRequestDto messageRequestDto) {
        roomService.disconnect(
            messageRequestDto.getType(),
            messageRequestDto.getRoomId(),
            messageRequestDto.getUserId()
        );
    }

    @MessageMapping("/chat/message")
    public void message(MessageRequestDto messageRequestDto) {
        roomService.convertAndSendMessage(
            messageRequestDto.getType(),
            messageRequestDto.getRoomId(),
            messageRequestDto.getUserId(),
            messageRequestDto.getMessage()
        );
    }

    @MessageExceptionHandler
    public String exception() {
        return "Error has occurred.";
    }
}
