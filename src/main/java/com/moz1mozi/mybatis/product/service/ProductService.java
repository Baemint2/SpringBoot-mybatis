package com.moz1mozi.mybatis.product.service;

import com.moz1mozi.mybatis.cart.dao.CartMapper;
import com.moz1mozi.mybatis.common.exception.OutOfStockException;
import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.product.dao.ProductMapper;
import com.moz1mozi.mybatis.product.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final CartMapper cartMapper;
    private final UserMapper userMapper;
    private final ImageService imageService;

    //상품 등록
    @Transactional
    public Long insertProduct(ProductDto productDto, List<MultipartFile> files, String username) throws IOException {
        Long sellerId = userMapper.findByMemberIdByUsername(username);
        UserDto role = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if (!"판매자".equals(role.getUserRole().getDisplayName())) {
            throw new IllegalArgumentException("상품 업로드는 판매자만 가능합니다.");
        }

        productDto.setSellerId(sellerId);
        productMapper.insertProduct(productDto);
        long productId = productDto.getProductId(); // 상품 ID를 조회하거나, insertProduct 메서드 내에서 ProductDto에 상품 ID를 설정
        // 이미지 리스트가 존재하는 경우, 각 이미지 정보를 저장
        if(files != null && !files.isEmpty()) {
            imageService.uploadFile(files, productId);
        }

        return productId;
    }

    public int deleteProduct(Long prodId) {
        return productMapper.deleteProduct(prodId);
    }

    public List<ProductListDto> findAllProducts() {
        return productMapper.findAllProducts();
    }

    // 상품 상세
    public List<ProductDetailDto> getProductByNo(Long productId) {
        return Optional.ofNullable(productMapper.getProductByNo(productId))
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

    }

    public Long updateProduct(Long prodId, ProductUpdateDto productUpdateDto, List<MultipartFile> files) throws IOException {
        ProductDto existingProduct = Optional.ofNullable(productMapper.selectProductById(prodId))
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. ID = " + prodId));

        existingProduct.update(productUpdateDto);

        productMapper.updateProduct(existingProduct);
        log.info("existingProduct = {}, {}", existingProduct.getImageDtoList(), existingProduct.getStoredUrl());
        if(files != null && !files.isEmpty()) {
            imageService.uploadFile(files, prodId);
        }

        return existingProduct.getProductId();
    }

    // 상품 검색 기능(새로운 클래스 적용해보기)
    @Async
    public CompletableFuture<ProductPageDto> searchProductsWithPagingAsync(String prodName, String nickname, Integer startPrice, Integer endPrice,
                                                                           int page, int pageSize, Long categoryId) {
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .prodName(prodName)
                .nickname(nickname)
                .startPrice(startPrice)
                .endPrice(endPrice)
                .page(page) // 페이지 번호
                .pageSize(pageSize) // 페이지 크기
                .categoryId(categoryId)
                .build();
        int offset = (searchDto.getPage() - 1) * searchDto.getPageSize();

        ProductSearchDto pagedSearchDto = searchDto.withPaging(searchDto.getPage(), searchDto.getPageSize());

        return CompletableFuture.supplyAsync(() -> {
            List<ProductDetailDto> products = productMapper.findByCondition(pagedSearchDto, pagedSearchDto.getPageSize(), offset);
            Long totalResultsLong = productMapper.countByCondition(pagedSearchDto);
            int totalResults = totalResultsLong.intValue();
            int totalPages = (int) Math.ceil((double) totalResults / pagedSearchDto.getPageSize());

            return new ProductPageDto(products, pagedSearchDto.getPage(), totalPages, totalResults, pagedSearchDto.getPageSize(), categoryId);
        });
    }

    // 판매자 권한 비교
    public boolean isUserSellerOfProduct(String username, Long productId) {
        String sellerUsername = productMapper.findSellerUsernameByProductId(productId);

        return username.equals(sellerUsername);
    }

    // 어드민 권한 비교
    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_관리자"));
    }



    @Transactional
    // 장바구니 제품 추가
    public int addToCartAndUpdateStockQuantity(Long productId, int quantity) {
        int stock = productMapper.getActualStockByProductId(productId);
        if(stock >= quantity) {
            int newStockQuantity = stock - quantity;
            updateStockQuantity(productId, newStockQuantity);
            return newStockQuantity;
        } else {
            throw new OutOfStockException("stock", "해당 상품의 재고가 부족합니다");

        }

    }

    @Transactional
    public void completeOrderAndUpdateStockQuantity(Long productId, int quantity) {
        int stock = productMapper.getStockByProductId(productId);
        if(stock >= quantity) {
            updateStockQuantity(productId, stock - quantity);
        } else {
            throw new OutOfStockException("stock", "해당 상품의 재고가 부족합니다");
        }
    }

    @Transactional
    public int getStockByProductId(Long productId) {
        return productMapper.getStockByProductId(productId);
    }

    // 상품 재고 증가
    @Transactional
    public void increaseStockQuantity(Long productId, int quantity) {
        int currentStock = productMapper.getStockByProductId(productId);
        int newStockQuantity = currentStock - quantity;
        updateStockQuantity(productId, newStockQuantity);
    }

    // 재고 감소
    @Transactional
    public void decreaseStockQuantity(Long productId, int quantity) {
        log.info("재고감소 메소드 호출 되나요?");
        int currentStock = productMapper.getStockByProductId(productId);
        log.info("현재 재고 = {}", currentStock);
        if(currentStock < quantity) {
            throw new OutOfStockException("stock", "해당 상품의 재고가 부족합니다");
        }
        int newStockQuantity = currentStock - quantity;
        log.info("수정된 재고 = {}", newStockQuantity);

        updateStockQuantity(productId, newStockQuantity);
    }

    @Transactional
    // 재고 수량 업데이트
    public void updateStockQuantity(Long productId, int adjustment) {
        log.info("재고 수량 업데이트 되나요?");
        StockUpdateDto stockUpdateDto = StockUpdateDto.builder()
                .productId(productId)
                .adjustment(adjustment)
                .build();
        log.info("{} {}", stockUpdateDto.getProductId(), stockUpdateDto.getAdjustment());
        productMapper.updateStockQuantity(stockUpdateDto);
    }


    @Transactional
    public void adjustStockQuantity(Long productId, int adjustment, boolean isIncrease) {
        log.info("{} {} {}", productId, adjustment, isIncrease);
        if(isIncrease) {
            // 수량 증가 시 카트 + 1 / 상품 재고 -1
            cartMapper.increaseCartItemQuantity(productId, adjustment);
            increaseStockQuantity(productId, adjustment);
        } else {
            // 수량 감소 시 카트 -1 / 상품 재고 + 1
            cartMapper.decreaseCartItemQuantity(productId, adjustment);
            decreaseStockQuantity(productId, adjustment);
        }
    }

}
