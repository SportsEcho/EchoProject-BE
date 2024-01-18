package com.sportsecho.gamechat;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {

    private final Map<Long, Room> rooms = new HashMap<>();

    @PostConstruct
    public void init() {
        createRooms();
    }

    public void createRooms() {
        rooms.put(1L, new Room("1번 채팅방"));
        rooms.put(2L, new Room("2번 채팅방"));
        rooms.put(3L, new Room("2번 채팅방"));
    }

    public Room findById(Long roomId) {
        return rooms.get(roomId);
    }
}