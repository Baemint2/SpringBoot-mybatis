package com.moz1mozi.mybatis.cart.service;

import com.moz1mozi.mybatis.cart.dao.CartDao;
import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import com.moz1mozi.mybatis.member.dao.MemberDao;
import com.moz1mozi.mybatis.product.dto.StockUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartDao cartDao;

    @Transactional
    public Long addCartItem(CartDto cartDto, Long memberId) {
        CartDto addCartDto = CartDto.builder()
                .memberId(memberId)
                .productId(cartDto.getProductId())
                .price(cartDto.getPrice())
                .quantity(cartDto.getQuantity())
                .build();

        return cartDao.insertCartItem(addCartDto);
    }

    //장바구니 확인
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartItemsByMemberId(Long memberId) {

        List<CartDetailDto> items = cartDao.findMyCartItems(memberId);
        if(items.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }
        return items;
    }

    @Transactional
    public TotalCartDto getTotalPrice(Long memberId) {
        return cartDao.getTotalPrice(memberId);
    }


}
