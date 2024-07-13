package com.moz1mozi.mybatis.cart.service;

import com.moz1mozi.mybatis.cart.mapper.CartMapper;
import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final ProductMapper productMapper;
    private final CartMapper cartMapper;

    @Transactional
    public Integer addCartItem(CartDto cartDto, Long userId) {
        CartDto addCartDto = CartDto.builder()
                .userId(userId)
                .prodId(cartDto.getProdId())
                .cartPrice(cartDto.getCartPrice())
                .cartQuantity(cartDto.getCartQuantity())
                .build();
        return cartMapper.insertCartItem(addCartDto);
    }

    // 상품 삭제
    @Transactional
    public void deleteCartAndUpdateStock(Long cartItemId) {
        CartDto cartDto = cartMapper.selectCartItemById(cartItemId);
        if(cartDto != null) {
            productMapper.increaseStockQuantity(cartDto.getProdId(), cartDto.getCartQuantity());
            cartMapper.deleteCartItem(cartItemId);
        } else {
            throw new CustomException("error","장바구니에 상품이 존재하지 않습니다.");
        }
    }

    // 상품 선택 삭제
    @Transactional
    public boolean deleteCartItems(String cartItemIds) {
        List<Long> ids = Arrays.stream(cartItemIds.split(","))
                .map(Long::parseLong)
                .toList();

        log.info("선택된 상품 : {}", ids);

        boolean allExist = ids.stream().allMatch(cartItemId -> {
                CartDto cartDto = cartMapper.selectCartItemById(cartItemId);
                if (cartDto != null) {
                    productMapper.increaseStockQuantity(cartDto.getProdId(), cartDto.getCartQuantity());
                    log.info("선택된 상품 ID : {}", cartDto.getCartId());
                    return true;
                } else {
                    return false;
                }
            }
        );

        log.info("상품 존재 여부: {}", allExist);
        if(!allExist) {
            return false;
        }

        cartMapper.deleteCartItems(ids);
        log.info("메서드 호출 되나요?" + ids);
        return true;
    }

    //장바구니 확인
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartItemsByMemberId(Long userId) {

        //        if (items.isEmpty()) {
//            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
//        }
        return cartMapper.findMyCartItems(userId);
    }

    @Transactional
    public TotalCartDto getTotalPrice(Long userId) {
        return cartMapper.getTotalPrice(userId);
    }


    // 장바구니에 상품 추가 또는 수량 업데이트
    @Transactional
    public Integer addOrUpdateCartItem(Long userId, Long prodId, Integer cartQuantity, int price) {
        log.info("addOrUpdateCartItem 호출");
        // 멤버 ID와 상품 ID로 현재 수량 조회
        Integer currentQuantity = cartMapper.findQuantity(userId, prodId);
        log.info("현재 수량 조회 = {}", currentQuantity);
        if (currentQuantity != null && currentQuantity > 0) {
            // 상품이 이미 존재하면 수량 업데이트
            cartMapper.increaseCartItemQuantity(prodId, cartQuantity);
            log.info("상품 ID = {}, 수량 = {}", prodId, cartQuantity);
            // 업데이트된 수량 반환
            return currentQuantity;
        } else {
            // 새로운 상품 추가
            CartDto cartDto = CartDto.builder()
                    .userId(userId)
                    .prodId(prodId)
                    .cartQuantity(cartQuantity)
                    .cartPrice(price)
                    .build();
            cartMapper.insertCartItem(cartDto);
            // 새로 추가된 항목의 수량 반환
            return cartQuantity;
        }

    }

//    public List<CartDetailDto> findCartItemsByIds(List<Long> cartItemIds) {
//
//    };
}
