package com.moz1mozi.mybatis.wishlist.mapper;

import com.moz1mozi.mybatis.wishlist.dto.WishlistDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WishlistMapper {
    int checkLike(Long memberId, Long productId);
    void addLike(Long memberId, Long productId);
    void removeLike(Long memberId, Long productId);
    boolean isLiked(Long memberId, Long productId);

    List<WishlistDto> getWishlistByMemberId(Long memberId);
}
