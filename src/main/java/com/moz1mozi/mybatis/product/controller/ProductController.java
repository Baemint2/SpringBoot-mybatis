package com.moz1mozi.mybatis.product.controller;

import com.moz1mozi.mybatis.product.dto.ProductDto;
import com.moz1mozi.mybatis.product.dto.ProductListDto;
import com.moz1mozi.mybatis.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/product")
    public String products() {
        return "product/product-insert";
    }


    @PostMapping("/api/v1/product/insert")
    public ResponseEntity<?> insertProducts(@RequestPart("product") ProductDto productDto,
                                            @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                            Authentication authentication) throws IOException {
        String username = authentication.getName();
        Long productId = productService.insertProduct(productDto, files, username);
        log.info("상품등록 = {}", productId);
        return ResponseEntity.ok(Map.of("productId", productId));
    }

    @GetMapping("/api/v1/product/list")
    public ResponseEntity<?> getProductList() {
        List<ProductListDto> products = productService.findAllProducts();
        return ResponseEntity.ok(Map.of("products", products));
    }


}
