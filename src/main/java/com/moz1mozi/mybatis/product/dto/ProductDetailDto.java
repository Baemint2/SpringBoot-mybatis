package com.moz1mozi.mybatis.product.dto;

import com.moz1mozi.mybatis.category.dto.CategoryDto;
import com.moz1mozi.mybatis.image.dto.ImageDto;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.dto.Role;
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
    private String prodName;
    private String description;
    private int prodPrice;
    private int stockQuantity;
    private Role role;
    private UserDto memberDto;
    private ImageDto imageDto;
    private CategoryDto categoryDto;
}
