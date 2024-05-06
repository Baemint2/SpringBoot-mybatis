package com.moz1mozi.mybatis.exception;

import lombok.Getter;

@Getter
public class InvalidCurrentPasswordException extends RuntimeException{

    private final String field;
    private final String message;

    public InvalidCurrentPasswordException(String field, String message) {
        super(message);
        this.field = field;
        this.message = message;
    }
}
