package com.sportsecho.gamechat;

public class RoomIdGenerator {

    private static Long id = 0L;

    public static Long createId() {
        return ++id;
    }
}