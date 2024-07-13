package com.moz1mozi.mybatis.wishlist.dto;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import com.moz1mozi.mybatis.product.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class WishlistDto {
    private Long wishId;
    private Long userId;
    private Long prodId;
    private boolean wishIsLiked;
    private ProductDto productDto;
    private ImageDto imageDto;
}
