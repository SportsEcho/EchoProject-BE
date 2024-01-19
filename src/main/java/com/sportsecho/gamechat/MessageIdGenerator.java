package com.sportsecho.gamechat;

public class MessageIdGenerator {

    private static Long id = 0L;

    public static Long generateId() {
        return ++id;
    }

}
