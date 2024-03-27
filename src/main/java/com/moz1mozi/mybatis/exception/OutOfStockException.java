package com.moz1mozi.mybatis.exception;

import lombok.Getter;

@Getter
public class OutOfStockException extends RuntimeException{

    private String field;
    private String message;

    public OutOfStockException(String field, String message) {
        super(message);
        this.field = field;
        this.message = message;
    }

}
