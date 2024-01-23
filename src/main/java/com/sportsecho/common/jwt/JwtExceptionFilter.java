package com.sportsecho.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsecho.global.exception.GlobalException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authorization 필터에서 발생하는 GlobalException은 HttpResponse로 반환되지 않는다.
 * JwtExceptionFilter를 별도로 정의해서 클라이언트에 GlobalException을 반환한다.
 * */
@Component
@Slf4j(topic = "JwtExceptionFilter")
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (GlobalException e) {
            log.error("ERROR: {}, URL: {}, MESSAGE: {}", e.getErrorCode(),
                request.getRequestURI(), e.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(
                response.getWriter(),
                ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage())
            );
        }
    }
}
