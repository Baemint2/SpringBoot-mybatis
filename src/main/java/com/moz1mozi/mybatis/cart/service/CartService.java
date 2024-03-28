package com.moz1mozi.mybatis.cart.service;

import com.moz1mozi.mybatis.cart.dao.CartDao;
import com.moz1mozi.mybatis.cart.dto.CartDetailDto;
import com.moz1mozi.mybatis.cart.dto.CartDto;
import com.moz1mozi.mybatis.cart.dto.TotalCartDto;
import com.moz1mozi.mybatis.exception.CustomException;
import com.moz1mozi.mybatis.product.dao.ProductDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final ProductDao productDao;
    private final CartDao cartDao;

    @Transactional
    public Integer addCartItem(CartDto cartDto, Long memberId) {
        CartDto addCartDto = CartDto.builder()
                .memberId(memberId)
                .productId(cartDto.getProductId())
                .price(cartDto.getPrice())
                .quantity(cartDto.getQuantity())
                .build();
        return cartDao.insertCartItem(addCartDto);
    }

    // 상품 삭제
    @Transactional
    public void deleteCartAndUpdateStock(Long cartItemId) {
        CartDto cartDto = cartDao.selectCartItemById(cartItemId);
        if(cartDto != null) {
            productDao.increaseStockQuantity(cartDto.getProductId(), cartDto.getQuantity());
            cartDao.deleteCartItem(cartItemId);
        } else {
            throw new CustomException("error","장바구니에 상품이 존재하지 않습니다.");
        }
    }

    //장바구니 확인
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartItemsByMemberId(Long memberId) {

        List<CartDetailDto> items = cartDao.findMyCartItems(memberId);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }
        return items;
    }

    @Transactional
    public TotalCartDto getTotalPrice(Long memberId) {
        return cartDao.getTotalPrice(memberId);
    }


    // 장바구니에 상품 추가 또는 수량 업데이트
    @Transactional
    public Integer addOrUpdateCartItem(Long memberId, Long productId, Integer quantity, int price) {
        log.info("addOrUpdateCartItem 호출");
        // 멤버 ID와 상품 ID로 현재 수량 조회
        Integer currentQuantity = cartDao.findQuantity(memberId, productId);
        log.info("현재 수량 조회 = {}", currentQuantity);
        if (currentQuantity != null && currentQuantity > 0) {
            // 상품이 이미 존재하면 수량 업데이트
            cartDao.increaseCartItemQuantity(productId, quantity);
            log.info("상품 ID = {}, 수량 = {}", productId, quantity);
            // 업데이트된 수량 반환
            return currentQuantity;
        } else {
            // 새로운 상품 추가
            CartDto cartDto = CartDto.builder()
                    .memberId(memberId)
                    .productId(productId)
                    .quantity(quantity)
                    .price(price)
                    .build();
            cartDao.insertCartItem(cartDto);
            // 새로 추가된 항목의 수량 반환
            return quantity;
        }

    }
}
