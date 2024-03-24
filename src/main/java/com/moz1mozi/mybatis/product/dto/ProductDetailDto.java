package com.moz1mozi.mybatis.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDetailDto {
    private long productId;
    private String storedUrl;
    private String prodName;
    private String description;
    private int prodPrice;
    private int stockQuantity;
    private String nickname;
}
