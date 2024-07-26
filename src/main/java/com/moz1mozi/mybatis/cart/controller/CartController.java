package com.moz1mozi.mybatis.cart.controller;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import com.moz1mozi.mybatis.cart.service.CartService;
import com.moz1mozi.mybatis.common.exception.OutOfStockException;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.UserService;
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

    private final UserService userService;

    @GetMapping("/user/cart")
    public String myCart(Model model, Principal principal) {
        String username = principal.getName();
        UserDto loggedUser = userService.findByUsername(username);
        Long userId = loggedUser.getUserId();
        log.info("로그인한 멤버 : {}", userId);
        model.addAttribute("loggedUser", loggedUser);

        List<CartDetailDto> itemsByMemberId = cartService.getCartItemsByMemberId(userId);
        if (itemsByMemberId.isEmpty()) {
            model.addAttribute("emptyCartMessage", "장바구니가 비어있습니다.");
        } else {
            model.addAttribute("myItems", itemsByMemberId);

        }
        return "cart/myCart";
    }

    // 상품 추가
    @PostMapping("/api/v1/cart")
    public ResponseEntity<?> addCartItems(@RequestBody CartDto cartDto, Principal principal) {
        try {
            String username = principal.getName();
            Long userId = userService.findByUsername(username).getUserId();
            int updateStock = productService.addToCartAndUpdateStockQuantity(cartDto.getProdId(), cartDto.getCartQuantity());
            Integer cartId = cartService.addOrUpdateCartItem(userId, cartDto.getProdId(), cartDto.getCartQuantity(), cartDto.getCartPrice());

            return ResponseEntity.ok().body(Map.of("cartId", cartId,
                    "prodId", cartDto.getProdId(),
                    "updatedStock", updateStock));
        } catch (OutOfStockException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 총 가격
    @GetMapping("/api/v1/cart")
    public ResponseEntity<?> getCartTotal(Principal principal) {
        String username = principal.getName();
        Long userId = userService.findByUsername(username).getUserId();

        TotalCartDto totalPrice = cartService.getTotalPrice(userId);
        return ResponseEntity.ok(totalPrice);
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/api/v1/cart/{cartId}")
    public ResponseEntity<Long> deleteCartItem(@PathVariable Long cartId) {
        cartService.deleteCartAndUpdateStock(cartId);
        return ResponseEntity.ok(cartId);
    }

    // 장바구니 상품 선택 삭제 (여러개 가능)
    @DeleteMapping("/api/v1/cart/item/{cartIds}")
    public ResponseEntity<?> deleteCartItems(@PathVariable String cartIds) {

        boolean deleted = cartService.deleteCartItems(cartIds);
        if(!deleted) {
            return ResponseEntity.badRequest().body("특정 아이템이 유효하지 않습니다.");
        }
        return ResponseEntity.ok().build();
    }

}
