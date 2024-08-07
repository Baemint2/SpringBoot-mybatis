package com.moz1mozi.mybatis.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductListDto {
    private int prodId;
    private String userNickname;
    private String prodName;
    private int prodPrice;
    private String storedUrl;
}
