package com.moz1mozi.mybatis.cart.service;

import com.moz1mozi.mybatis.cart.mapper.CartMapper;
import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.user.service.MemberService;
import com.moz1mozi.mybatis.product.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartMapper cartMapper;

    public static final Long userId = 18L;
    @Test
    void 장바구니등록_테스트() {

        CartDto cartDto = CartDto.builder()
                .userId(userId)
                .prodId(27L)
                .cartQuantity(3)
                .cartPrice(1000000)
                .cartDateAdded(Date.from(Instant.now()))
                .build();

        Integer results = cartService.addCartItem(cartDto, userId);
        assertNotNull(results);
    }

    @Test
    void 내장바구니() {

        memberService.findByUsername("emozi");
        List<CartDetailDto> itemsByMemberId = cartService.getCartItemsByMemberId(userId);
        assertNotNull(itemsByMemberId);
    }
    @Test
    void 내장바구니_실패() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cartService.getCartItemsByMemberId(userId));
        String exceptionMessage = "장바구니가 비어 있습니다.";
        String message = exception.getMessage();
        assertTrue(message.contains(exceptionMessage));
    }

    @Test
    void 내장바구니_실패_() {
        List<CartDetailDto> itemsByMemberId = cartService.getCartItemsByMemberId(userId);
        assertTrue(itemsByMemberId.isEmpty());
    }

    @Test
    void 장바구니_상품_삭제() {
        Long cartItemId = 76L;
        CartDto cartDto = cartMapper.selectCartItemById(cartItemId);
        assertNotNull(cartDto);

        cartService.deleteCartAndUpdateStock(cartItemId);

        assertNull(cartMapper.selectCartItemById(cartItemId));
    }
}