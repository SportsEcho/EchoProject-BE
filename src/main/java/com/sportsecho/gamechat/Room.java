package com.sportsecho.gamechat;

import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class Room {

    private final Long id;
    private final String name;

    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public Room(String name) {
        this.id = RoomIdGenerator.createId();
        this.name = name;
    }
}
