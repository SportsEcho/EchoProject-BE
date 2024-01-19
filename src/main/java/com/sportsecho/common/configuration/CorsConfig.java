package com.sportsecho.common.configuration;

import com.sportsecho.common.jwt.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://sportsecho.life:3000")
            .exposedHeaders(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.REFRESH_AUTHORIZATION_HEADER);
    }
}

