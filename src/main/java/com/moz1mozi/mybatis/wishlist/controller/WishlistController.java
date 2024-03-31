package com.moz1mozi.mybatis.wishlist.controller;

import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.wishlist.dto.WishlistDto;
import com.moz1mozi.mybatis.wishlist.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WishlistController {

    private final WishListService wishListService;
    private final MemberService memberService;

    @PostMapping("/api/v1/wishlist/toggle/{productId}")
    public ResponseEntity<?> toggleWishlist(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long memberId = memberService.findByUsername(username).getMemberId();
        boolean isLiked = wishListService.toggleLike(memberId, productId);
        return ResponseEntity.ok(isLiked);
    }

    @GetMapping("/api/v1/wishlist")
    public ResponseEntity<List<WishlistDto>> getWishlist() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long memberId = memberService.findByUsername(username).getMemberId();
        List<WishlistDto> wishlist = wishListService.getWishlistByMember(memberId);
        return ResponseEntity.ok(wishlist);
    }

}
