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
    private Long cartId;
    private Long userId;
    private Long prodId;
    private Integer cartQuantity;
    private Integer cartPrice;
    private Date cartDateAdded;
}
