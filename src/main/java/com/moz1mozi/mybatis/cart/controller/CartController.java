package com.moz1mozi.mybatis.cart.controller;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import com.moz1mozi.mybatis.cart.service.CartService;
import com.moz1mozi.mybatis.exception.OutOfStockException;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    public String myCart(Model model, Principal principal) {
        String username = principal.getName();
        MemberDto loggedUser = memberService.findByUsername(username);
        Long memberId = loggedUser.getMemberId();
        log.info("로그인한 멤버 : {}", memberId);
        model.addAttribute("loggedUser", loggedUser);

        List<CartDetailDto> itemsByMemberId = cartService.getCartItemsByMemberId(memberId);
        if (itemsByMemberId.isEmpty()) {
            model.addAttribute("emptyCartMessage", "장바구니가 비어있습니다.");
        } else {
            model.addAttribute("myItems", itemsByMemberId);

        }
        return "cart/myCart";
    }

    // 상품 추가
    @PostMapping("/api/v1/cart/add")
    public ResponseEntity<?> addCartItems(@RequestBody CartDto cartDto, Principal principal) {
        try {
            String username = principal.getName();
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
    public ResponseEntity<?> getCartTotal(Principal principal) {
        String username = principal.getName();
        Long memberId = memberService.findByUsername(username).getMemberId();

        TotalCartDto totalPrice = cartService.getTotalPrice(memberId);
        return ResponseEntity.ok(totalPrice);
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/api/v1/cart/{cartItemId}")
    public ResponseEntity<Long> deleteCartItem(@PathVariable Long cartItemId) {
        cartService.deleteCartAndUpdateStock(cartItemId);
        return ResponseEntity.ok(cartItemId);
    }

    // 장바구니 상품 선택 삭제 (여러개 가능)
    @DeleteMapping("/api/v1/cart/item/{cartItemIds}")
    public ResponseEntity<?> deleteCartItems(@PathVariable String cartItemIds) {

        boolean deleted = cartService.deleteCartItems(cartItemIds);
        if(!deleted) {
            return ResponseEntity.badRequest().body("특정 아이템이 유효하지 않습니다.");
        }
        return ResponseEntity.ok().build();
    }

}
