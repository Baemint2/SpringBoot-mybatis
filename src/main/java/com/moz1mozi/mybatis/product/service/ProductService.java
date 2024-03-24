package com.moz1mozi.mybatis.product.service;

import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.member.dao.MemberDao;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.product.dao.ProductDao;
import com.moz1mozi.mybatis.product.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
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

    private final ProductDao productDao;
    private final MemberDao memberDao;
    private final ImageService imageService;

    //상품 등록
    @Transactional
    public Long insertProduct(ProductDto productDto, List<MultipartFile> files, String username) throws IOException {
        Long sellerId = memberDao.findByMemberIdByUsername(username);
        MemberDto role = memberDao.findByUsername(username);
        if (!"판매자".equals(role.getRole().getDisplayName())) {
            throw new IllegalArgumentException("상품 업로드는 판매자만 가능합니다.");
        }

        productDto.setSellerId(sellerId);
        productDao.insertProduct(productDto);
        long productId = productDto.getProductId(); // 상품 ID를 조회하거나, insertProduct 메서드 내에서 ProductDto에 상품 ID를 설정
        // 이미지 리스트가 존재하는 경우, 각 이미지 정보를 저장
        if(files != null && !files.isEmpty()) {
            imageService.uploadFile(files, productId);
        }

        return productId;
    }

    public int deleteProduct(Long prodId) {
        return productDao.deleteProduct(prodId);
    }

    public List<ProductListDto> findAllProducts() {
        return productDao.findAllProducts();
    }

    // 상품 상세
    public List<ProductDetailDto> getProductByNo(int productId) {
        return Optional.ofNullable(productDao.getProductByNo(productId))
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

    }

    // 페이징 처리
    public ProductPageDto getPagedProducts(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProductDetailDto> products = productDao.getPagedProducts(pageSize, offset);
        Long totalProducts = productDao.countAllProducts();
        int totalPages = (int)Math.ceil((double) totalProducts / pageSize);

        return new ProductPageDto(products, page, totalPages, totalProducts, pageSize);
    }

    public Long updateProduct(Long prodId, ProductUpdateDto productUpdateDto, List<MultipartFile> files) throws IOException {
        ProductDto existingProduct = Optional.ofNullable(productDao.selectProductById(prodId))
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. ID = " + prodId));

        existingProduct.update(productUpdateDto);

        productDao.updateProduct(existingProduct);
        log.info("existingProduct = {}, {}", existingProduct.getImageDtoList(), existingProduct.getStoredUrl());
        if(files != null && !files.isEmpty()) {
            imageService.uploadFile(files, prodId);
        }

        return existingProduct.getProductId();
    }

    // 상품 검색 기능(새로운 클래스 적용해보기)
    @Async
    public CompletableFuture<ProductPageDto> searchProductsWithPagingAsync(String prodName, String nickname, Integer startPrice, Integer endPrice,
                                                                           int page, int pageSize) {
        ProductSearchDto searchDto = ProductSearchDto.builder()
                .prodName(prodName)
                .nickname(nickname)
                .startPrice(startPrice)
                .endPrice(endPrice)
                .page(page) // 페이지 번호
                .pageSize(pageSize) // 페이지 크기
                .build();
        int offset = (searchDto.getPage() - 1) * searchDto.getPageSize();

        // 페이징 정보를 설정한 새로운 DTO 인스턴스 생성
        ProductSearchDto pagedSearchDto = searchDto.withPaging(searchDto.getPage(), searchDto.getPageSize());
        // 검색 조건을 포함하는 ProductSearchDto 객체 생성

        return CompletableFuture.supplyAsync(() -> {
            List<ProductDetailDto> products = productDao.findByCondition(pagedSearchDto, pagedSearchDto.getPageSize(), offset);
            Long totalResults = productDao.countByCondition(pagedSearchDto);
            int totalPages = (int) Math.ceil((double) totalResults / pagedSearchDto.getPageSize());

            return new ProductPageDto(products, pagedSearchDto.getPage(), totalPages, totalResults, pagedSearchDto.getPageSize());
        });
    }
}
