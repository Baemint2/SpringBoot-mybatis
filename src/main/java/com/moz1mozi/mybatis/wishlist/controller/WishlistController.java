package com.moz1mozi.mybatis.wishlist.controller;

import com.moz1mozi.mybatis.user.service.MemberService;
import com.moz1mozi.mybatis.wishlist.dto.WishlistDto;
import com.moz1mozi.mybatis.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WishlistController {

    private final WishListService wishListService;
    private final MemberService memberService;

    @PostMapping("/api/v1/wishlist/toggle/{prodId}")
    public ResponseEntity<?> toggleWishlist(@PathVariable Long prodId, Principal principal) {
        String username = principal.getName();
        Long userId = memberService.findByUsername(username).getUserId();
        boolean isLiked = wishListService.toggleLike(userId, prodId);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/api/v1/wishlist")
    public ResponseEntity<List<WishlistDto>> getWishlist(Principal principal) {
        String username = principal.getName();
        Long userId = memberService.findByUsername(username).getUserId();
        List<WishlistDto> wishlist = wishListService.getWishlistByMember(userId);
        return ResponseEntity.ok(wishlist);
    }

}
