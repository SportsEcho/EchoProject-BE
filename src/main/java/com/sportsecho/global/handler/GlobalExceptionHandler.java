package com.sportsecho.global.handler;

import com.sportsecho.comment.exception.CommentException;
import com.sportsecho.global.exception.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<String> handleExternalApiException(GlobalException e) {
        HttpStatus status = e.getErrorCode().getStatus();
        return ResponseEntity.status(status).body(e.getMessage());
    }
}
