package com.moz1mozi.mybatis.cart.service;

import com.moz1mozi.mybatis.cart.dao.CartDao;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartDao cartDao;

    @Transactional()
    public Long addCartItem(CartDto cartDto) {
        return cartDao.insertCartItem(cartDto);
    }

}
