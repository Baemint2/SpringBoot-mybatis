package com.moz1mozi.mybatis.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handlerCustomException(CustomException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getField(), ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handlerIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", ex.getMessage());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "잘못된 요청 형식입니다.";
        // 예외 로깅
        if(ex.getCause() instanceof InvalidFormatException) {
            message = "상품가격 또는 상품수량은 숫자를 입력하세요.";
        }
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", message);
        return ResponseEntity.badRequest().body(errorDetails);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Object> handlerInvalidFormatException(InvalidFormatException ex) {
        String message = "상품가격 또는 상품수량은 숫자를 입력하세요.";
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", message);
        return ResponseEntity.badRequest().body(errorDetails);
    }
}
