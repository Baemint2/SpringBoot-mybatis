package com.moz1mozi.mybatis.wishlist.service;

import com.moz1mozi.mybatis.wishlist.dao.WishlistMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishlistMapper wishlistMapper;
    @Transactional
    public boolean toggleLike(Long memberId, Long productId) {
        int count = wishlistMapper.checkLike(memberId, productId);

        if(count > 0) {
            // 이미 좋아요 누른 상태
            wishlistMapper.removeLike(memberId, productId);
            return false;
        } else {
            wishlistMapper.addLike(memberId, productId);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long memberId, Long productId) {
        return wishlistMapper.isLiked(memberId, productId);
    }
}
