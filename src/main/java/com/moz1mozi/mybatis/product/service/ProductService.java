package com.moz1mozi.mybatis.product.service;

import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.member.dao.MemberDao;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.product.dao.ProductDao;
import com.moz1mozi.mybatis.product.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        MemberDto role = memberDao.findByRoleWithUsername(username);
        if (!"판매자".equals(role.getRole().getDisplayName())) {
            throw new IllegalArgumentException("상품 업로드는 판매자만 가능합니다.");
        }

        productDto.setSellerId(sellerId);
        productDao.insertProduct(productDto);
        long productId = productDto.getProductId(); // 상품 ID를 조회하거나, insertProduct 메서드 내에서 ProductDto에 상품 ID를 설정
        // 이미지 리스트가 존재하는 경우, 각 이미지 정보를 저장
        if(files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                imageService.uploadFile(file, productId);
            }
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
    public ProductDetailDto getProductByNo(int productId) {
        return Optional.ofNullable(productDao.getProductByNo(productId))
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

    }

    // 페이징 처리
    public ProductPageDto getPagedProducts(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<ProductListDto> products = productDao.getPagedProducts(pageSize, offset);
        Long totalProducts = productDao.countAllProducts();
        int totalPages = (int)Math.ceil((double) totalProducts / pageSize);

        return new ProductPageDto(products, page, totalPages, totalProducts, pageSize);
    }

    public Long updateProduct(Long prodId, ProductUpdateDto productUpdateDto, MultipartFile file) throws IOException {
        ProductDto existingProduct = Optional.ofNullable(productDao.selectProductById(prodId))
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다. ID = " + prodId));

        existingProduct.update(productUpdateDto);

        productDao.updateProduct(existingProduct);

        if(file != null && !file.isEmpty()) {
            imageService.uploadFile(file, prodId);
        }

        return existingProduct.getProductId();
    }
}
