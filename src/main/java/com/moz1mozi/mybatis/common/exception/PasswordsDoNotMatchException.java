package com.moz1mozi.mybatis.common.exception;

import lombok.Getter;

@Getter
public class PasswordsDoNotMatchException extends RuntimeException{

    private final String field;
    private final String message;

    public PasswordsDoNotMatchException(String field, String message) {
        super(message);
        this.field = field;
        this.message = message;
    }
}
