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
    private Long productId;
    private Long cartItemId; // 이 필드는 쿼리 결과에 없으므로 사용하지 않거나 다른 방법을 찾아야 할 수 있습니다.
    private String prodName;
    private int totalQuantity;
    private int totalPrice; // 문자열 대신 정수나 실수 타입 사용
    private String storedUrl;
    private Date lastAdded;
}
