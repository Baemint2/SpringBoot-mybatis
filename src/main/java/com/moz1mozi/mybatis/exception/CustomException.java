package com.moz1mozi.mybatis.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final String field;
    private final String message;

    public CustomException(String field, String message){
        super(message);
        this.field = field;
        this.message = message;
    }
}
