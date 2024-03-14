package com.moz1mozi.mybatis.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDto {
    private long productId;
    private String prodName;
    private String description;
    private int prodPrice;
    private int stockQuantity;
    private Date createdAt;
    private Date modifiedAt;
}
