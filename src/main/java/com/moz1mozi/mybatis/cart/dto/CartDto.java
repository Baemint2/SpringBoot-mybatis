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
public class CartDto {
    private Long cartItemId;
    private Long memberId;
    private Long productId;
    private Integer quantity;
    private Integer price;
    private Date dataAdded;
}
