package com.moz1mozi.mybatis.exception;

import lombok.Getter;

@Getter
public class PasswordsDoNotMatchException extends RuntimeException{

    private final String field;
    private final String message;

    public PasswordsDoNotMatchException(String message, String field) {
        super(message);
        this.field = field;
        this.message = message;
    }
}
