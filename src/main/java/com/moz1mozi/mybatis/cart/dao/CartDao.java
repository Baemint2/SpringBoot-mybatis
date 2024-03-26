package com.moz1mozi.mybatis.cart.dao;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartDao {

    long insertCartItem(CartDto cartDto);

    List<CartDetailDto> findMyCartItems(Long memberId);

    // 남은 재고
    int getStockByProductId(int productId);
}
