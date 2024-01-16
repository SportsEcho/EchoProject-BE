package com.sportsecho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SportsEchoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportsEchoApplication.class, args);
    }

}
