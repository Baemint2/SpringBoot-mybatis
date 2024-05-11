package com.moz1mozi.mybatis.common.exception;

import lombok.Getter;

@Getter
public class OutOfStockException extends RuntimeException{

    private final String field;
    private final String message;

    public OutOfStockException(String field, String message) {
        super(message);
        this.field = field;
        this.message = message;
    }

}
