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
    private Long cartItemId;
    private String prodName;
    private int price;
    private int quantity;
    private String storedUrl;
    private Date dataAdded;

}
