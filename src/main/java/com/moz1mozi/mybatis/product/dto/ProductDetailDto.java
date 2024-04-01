package com.moz1mozi.mybatis.product.dto;

import com.moz1mozi.mybatis.member.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String username;
    private Role role;
    private int categoryId;
    private String categoryName;
}
