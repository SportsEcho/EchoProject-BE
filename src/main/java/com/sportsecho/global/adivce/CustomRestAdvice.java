package com.sportsecho.global.adivce;

import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.global.response.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> userExceptionHandler(GlobalException e) {
        log.error("Error occurs {}", e.toString());

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name(), e.getMsg()));
    }

    //컨트롤러 유효성 검증 예외처리 핸들러
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException e) {
        log.error("Error occurs {}", e.toString());

        Map<String, String> errorMap = new HashMap<>();

        if (e.hasErrors()) {
            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                .body(Response.error(HttpStatus.EXPECTATION_FAILED.name(), errorMap));
    }
}
