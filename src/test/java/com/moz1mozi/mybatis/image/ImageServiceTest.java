package com.moz1mozi.mybatis.image;

import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.product.dto.ProductDto;
import com.moz1mozi.mybatis.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

@SpringBootTest
class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductService productService;

    @Test
    void 파일업로드() throws IOException {

        ProductDto productDto = ProductDto.builder()
                .prodName("모지화장품")
                .description("안녕하세요 첫 출시 해봤습니다.")
                .prodPrice(5000)
                .stockQuantity(5)
                .createdAt(Date.from(Instant.now()))
                .build();
//        Long prodId = productService.insertProduct(productDto);


        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/lockdown.png"));
        MultipartFile multipartFile = new MockMultipartFile("file", "lockdown.png", "image/png", content);

//        imageService.uploadFile(multipartFile, prodId);
    }

}