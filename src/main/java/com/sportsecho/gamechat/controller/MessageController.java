package com.sportsecho.gamechat.controller;

import com.sportsecho.gamechat.Greeting;
import com.sportsecho.gamechat.HelloMessage;
import com.sportsecho.gamechat.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@Slf4j(topic = "MessageController")
@RequiredArgsConstructor
public class MessageController {

    private final RoomService roomService;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        log.info("test message1");

        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
