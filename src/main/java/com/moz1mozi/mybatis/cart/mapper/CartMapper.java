package com.moz1mozi.mybatis.cart.mapper;

import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    int insertCartItem(CartDto cartDto);

    List<CartDetailDto> findMyCartItems(Long userId);

    //전체 값
    TotalCartDto getTotalPrice(Long userId);

    CartDto selectCartItemById(Long carId);

    void increaseCartItemQuantity(@Param("prodId") Long prodId, @Param("cartQuantity") Integer cartQuantity);
    void decreaseCartItemQuantity(@Param("prodId") Long prodId, @Param("cartQuantity") Integer cartQuantity);


    // 상품 수량 여부 확인
    Integer findQuantity(Long userId, Long prodId);

    // 장바구니에서 제거
    int deleteCartItem(Long cartId);

    // 장바구니에서 여러 상품 제거
    int deleteCartItems(List<Long> cartIds);

    // 상품 존재 여부 확인
    boolean existsById(Long cartId);

    // 주문 화면으로 이동
    List<CartDetailDto> findCartItemsByIds(List<Long> cartIds);
}


