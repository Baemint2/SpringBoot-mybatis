package com.moz1mozi.mybatis.product.service;

import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.member.dao.MemberDao;
import com.moz1mozi.mybatis.product.dao.ProductDao;
import com.moz1mozi.mybatis.product.dto.ProductDto;
import com.moz1mozi.mybatis.product.dto.ProductListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
}
