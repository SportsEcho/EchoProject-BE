package com.sportsecho.gamechat;

import java.io.IOException;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "SocketTextHandler")
public class SocketTextHandler extends TextWebSocketHandler {

    private final RoomRepository roomRepository;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        Set<WebSocketSession> sessions = getRoom(session).getSessions();

        sessions.add(session);

        log.info("connect");
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) throws IOException {
        log.info("send message: {}", message.getPayload());

        Set<WebSocketSession> sessions = getRoom(session).getSessions();

        for (WebSocketSession connectedSession : sessions) {
            connectedSession.sendMessage(message);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        Set<WebSocketSession> sessions = getRoom(session).getSessions();

        sessions.remove(session);

        log.info("disconnect");
    }

    private Room getRoom(WebSocketSession session) {
        Long roomId = Long.parseLong(session.getAttributes().get("roomId").toString());
        return roomRepository.findById(roomId);
    }
}