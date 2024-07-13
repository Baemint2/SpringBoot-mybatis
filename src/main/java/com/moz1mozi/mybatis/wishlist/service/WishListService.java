package com.moz1mozi.mybatis.wishlist.service;

import com.moz1mozi.mybatis.wishlist.mapper.WishlistMapper;
import com.moz1mozi.mybatis.wishlist.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishlistMapper wishlistMapper;
    @Transactional
    public boolean toggleLike(Long userId, Long prodId) {
        int count = wishlistMapper.checkLike(userId, prodId);

        if(count > 0) {
            // 이미 좋아요 누른 상태
            wishlistMapper.removeLike(userId, prodId);
            return false;
        } else {
            wishlistMapper.addLike(userId, prodId);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long prodId) {
        return wishlistMapper.isLiked(userId, prodId);
    }

    @Transactional(readOnly = true)
    public List<WishlistDto> getWishlistByMember(Long userId) {
        return wishlistMapper.getWishlistByMemberId(userId);
    }
}
