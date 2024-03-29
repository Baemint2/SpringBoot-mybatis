package com.moz1mozi.mybatis.wishlist.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WishlistMapper {
    int checkLike(Long memberId, Long productId);
    void addLike(Long memberId, Long productId);
    void removeLike(Long memberId, Long productId);
    boolean isLiked(Long memberId, Long productId);
}
