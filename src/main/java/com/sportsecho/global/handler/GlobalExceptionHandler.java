package com.sportsecho.global.handler;

import com.sportsecho.comment.exception.CommentException;
import com.sportsecho.global.exception.ExternalApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<String> handleExternalApiException(ExternalApiException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(e.getMessage());
    }
    @ExceptionHandler(CommentException.class)
    public ResponseEntity<Object> handleCommentException(CommentException e) {
        HttpStatus status = e.getErrorCode().getStatus();
        String message = e.getErrorCode().getMsg();

        return ResponseEntity
            .status(status)
            .body(message);
    }
}
