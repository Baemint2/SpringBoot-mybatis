package com.moz1mozi.mybatis.wishlist.mapper;

import com.moz1mozi.mybatis.wishlist.dto.WishlistDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WishlistMapper {
    int checkLike(Long userId, Long prodId);
    void addLike(Long userId, Long prodId);
    void removeLike(Long userId, Long prodId);
    boolean isLiked(Long userId, Long prodId);

    List<WishlistDto> getWishlistByMemberId(Long userId);
}
