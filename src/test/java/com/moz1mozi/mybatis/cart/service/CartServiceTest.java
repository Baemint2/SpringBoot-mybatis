package com.moz1mozi.mybatis.cart.service;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

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
    public static final Long memberId = 18L;
    @Test
    void 장바구니등록_테스트() {

        CartDto cartDto = CartDto.builder()
                .memberId(memberId)
                .productId(27L)
                .quantity(3)
                .price(1000000)
                .dataAdded(Date.from(Instant.now()))
                .build();

        Long results = cartService.addCartItem(cartDto, memberId);
        assertNotNull(results);
    }

    @Test
    void 내장바구니() {

        memberService.findByUsername("emozi");
        List<CartDetailDto> itemsByMemberId = cartService.getCartItemsByMemberId(memberId);
        assertNotNull(itemsByMemberId);
    }
    @Test
    void 내장바구니_실패() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cartService.getCartItemsByMemberId(memberId));
        String exceptionMessage = "장바구니가 비어 있습니다.";
        String message = exception.getMessage();
        assertTrue(message.contains(exceptionMessage));
    }

    @Test
    void 내장바구니_실패_() {
        List<CartDetailDto> itemsByMemberId = cartService.getCartItemsByMemberId(memberId);
        assertTrue(itemsByMemberId.isEmpty());
    }
}