package com.sportsecho.gamechat.service;

import com.sportsecho.gamechat.MessageIdGenerator;
import com.sportsecho.gamechat.dto.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void convertAndSendMessage(String type, Long roomId, Long userId, String message) {
        simpMessagingTemplate.convertAndSend("/subscription/chat/room/" + roomId, new MessageResponseDto(
                MessageIdGenerator.generateId(),
                type,
                "사용자 " + userId + ": " + message
            )
        );
    }

    public void connect(String type, Long roomId, Long userId) {
        simpMessagingTemplate.convertAndSend("/subscription/chat/room/" + roomId, new MessageResponseDto(
                MessageIdGenerator.generateId(),
                type,
                "사용자 " + userId + " 님이 " + "채팅방 " + roomId + "에 입장하셨습니다."
            )
        );
    }

    public void disconnect(String type, Long roomId, Long userId) {
        simpMessagingTemplate.convertAndSend("/subscription/chat/room/" + roomId, new MessageResponseDto(
                MessageIdGenerator.generateId(),
                type,
                "사용자 " + userId + " 님이 " + "채팅방 " + roomId + "에서 나갔습니다."
            )
        );
    }

}
