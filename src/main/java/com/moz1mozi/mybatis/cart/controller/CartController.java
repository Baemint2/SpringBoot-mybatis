package com.moz1mozi.mybatis.cart.controller;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import com.moz1mozi.mybatis.cart.service.CartService;
import com.moz1mozi.mybatis.exception.OutOfStockException;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@Slf4j
public class CartController {

    private final ProductService productService;

    private final CartService cartService;

    private final MemberService memberService;
    @GetMapping("/member/cart")
    public String myCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long memberId = memberService.findByUsername(username).getMemberId();
        log.info("로그인한 멤버 : {}", memberId);

        List<CartDetailDto> itemsByMemberId = cartService.getCartItemsByMemberId(memberId);
        model.addAttribute("myItems", itemsByMemberId);
        return "cart/myCart";
    }

    // 상품 추가
    @PostMapping("/api/v1/cart/add")
    public ResponseEntity<?> addCartItems(@RequestBody CartDto cartDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Long memberId = memberService.findByUsername(username).getMemberId();
            int updateStock = productService.addToCartAndUpdateStockQuantity(cartDto.getProductId(), cartDto.getQuantity());
            Integer cartItemId = cartService.addOrUpdateCartItem(memberId, cartDto.getProductId(), cartDto.getQuantity(), cartDto.getPrice());

            return ResponseEntity.ok().body(Map.of("cartItemId", cartItemId,
                    "productId", cartDto.getProductId(),
                    "updatedStock", updateStock));
        } catch (OutOfStockException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 총 가격
    @GetMapping("/api/v1/cart/total")
    public ResponseEntity<?> getCartTotal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long memberId = memberService.findByUsername(username).getMemberId();

        TotalCartDto totalPrice = cartService.getTotalPrice(memberId);
        return ResponseEntity.ok(totalPrice);
    }

//     장바구니 상품 삭제
    @DeleteMapping("/api/v1/cart/{cartItemId}")
    public ResponseEntity<Long> deleteCartItem(@PathVariable Long cartItemId) {
        cartService.deleteCartAndUpdateStock(cartItemId);
        return ResponseEntity.ok(cartItemId);
    }

}
