package com.moz1mozi.mybatis.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CartDetailDto {
    private Long prodId;
    private Long cartId;
    private Long userId;
    private String prodName;
    private int totalQuantity;
    private int totalPrice; // 문자열 대신 정수나 실수 타입 사용
    private String piStoredUrl; //
    private Date lastAdded;
}
