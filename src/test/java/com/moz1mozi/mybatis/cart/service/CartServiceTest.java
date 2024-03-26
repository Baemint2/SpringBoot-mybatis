package com.moz1mozi.mybatis.cart.service;

import com.moz1mozi.mybatis.cart.dto.CartDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Test
    void 장바구니등록_테스트() {
        CartDto cartDto = CartDto.builder()
                .memberId(18L)
                .productId(27L)
                .quantity(3)
                .price(1000000)
                .dataAdded(Date.from(Instant.now()))
                .build();

        Long results = cartService.addCartItem(cartDto);
        assertNotNull(results);
    }
}