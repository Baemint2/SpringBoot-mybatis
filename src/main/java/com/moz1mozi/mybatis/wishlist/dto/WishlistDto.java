package com.moz1mozi.mybatis.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class WishlistDto {
    private Long wishlistId;
    private Long memberId;
    private Long productId;
    private boolean isLiked;
    private String prodName;
    private Integer prodPrice;
    private String storedUrl;
}
