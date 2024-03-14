package com.moz1mozi.mybatis.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {


    @Autowired
    private ProductService productService;

    @Test
    void 상품등록테스트() {
        ProductDto productDto = ProductDto.builder()
                .prodName("모지화장품")
                .description("안녕하세요 첫 출시 해봤습니다.")
                .prodPrice(5000)
                .stockQuantity(5)
                .createdAt(Date.from(Instant.now()))
                .build();

        Long result = productService.insertProduct(productDto);
        assertEquals(1, result);
    }

    @Test
    void 상품삭제() {
        int deleteCount = productService.deleteProduct(1L);
        assertEquals(1, deleteCount);
    }

}