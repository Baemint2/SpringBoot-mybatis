package com.moz1mozi.mybatis.cart.dao;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartDao {

    long insertCartItem(CartDto cartDto);

    List<CartDetailDto> findMyCartItems(Long memberId);

    //전체 값
    TotalCartDto getTotalPrice(Long memberId);

    void increaseCartItemQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    void decreaseCartItemQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);}
