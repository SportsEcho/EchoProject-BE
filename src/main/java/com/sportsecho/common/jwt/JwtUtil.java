package com.sportsecho.common.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

    @Value("${jwt.secret_key}")
    private String secretKey;

    //TODO
}
