package com.moz1mozi.mybatis.cart.controller;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.service.CartService;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@Slf4j
public class CartController {

    private final CartService cartService;
    @GetMapping("/member/cart")
    public String myCart(Model model) {
        List<CartDetailDto> itemsByMemberId = cartService.getCartItemsByMemberId();
        model.addAttribute("myItems", itemsByMemberId);
        return "cart/myCart";
    }

    @PostMapping("/api/v1/cart/add")
    public ResponseEntity<?> addCartItems(@RequestBody CartDto cartDto) {
        Long cartItemId = cartService.addCartItem(cartDto);
        return ResponseEntity.ok().body(Map.of("cartItemId", cartItemId)); // 성공 응답
    }

    // 남은 재고
    @GetMapping("/api/v1/product/{productId}/stock")
    public ResponseEntity<Integer> getStockByProductId(@PathVariable int productId) {
        int stock = cartService.getStockByProductId(productId);
        return ResponseEntity.ok(stock);
    }
}
