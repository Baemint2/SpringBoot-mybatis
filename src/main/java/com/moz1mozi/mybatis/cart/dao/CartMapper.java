package com.moz1mozi.mybatis.cart.dao;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    int insertCartItem(CartDto cartDto);

    List<CartDetailDto> findMyCartItems(Long memberId);

    //전체 값
    TotalCartDto getTotalPrice(Long memberId);

    CartDto selectCartItemById(Long cartItemId);

    void increaseCartItemQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);
    void decreaseCartItemQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);


    // 상품 수량 여부 확인
    Integer findQuantity(Long memberId, Long productId);

    // 장바구니에서 제거
    int deleteCartItem(Long cartItemId);

    // 장바구니에서 여러 상품 제거
    int deleteCartItems(List<Long> cartItemIds);

    // 상품 존재 여부 확인
    boolean existsById(Long cartItemId);

    // 주문 화면으로 이동
    List<CartDetailDto> findCartItemsByIds(List<Long> cartItemsIds);
}


