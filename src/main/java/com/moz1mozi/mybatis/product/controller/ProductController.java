package com.moz1mozi.mybatis.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.product.dto.*;
import com.moz1mozi.mybatis.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final MemberService memberService;

    @GetMapping("/product")
    public String products() {
        return "product/product-insert";
    }

    @GetMapping("/product/detail/{productId}")
    public String productDetail(@PathVariable int productId,
                                Model model,
                                Principal principal) {
        if(principal != null) {
            MemberDto member = memberService.findByUsername(principal.getName());
            model.addAttribute("member", member);

        }
        ProductDetailDto productByNo = productService.getProductByNo(productId);
        model.addAttribute("productByNo", productByNo);

        return "product/product-detail";
    }

    @GetMapping("/product/modify/{productId}")
    public String productModify(@PathVariable int productId,
                                Model model) {
        ProductDetailDto productByNo = productService.getProductByNo(productId);
        model.addAttribute("productByNo", productByNo);

        return "product/product-modify";
    }


    //상품 등록
    @PostMapping("/api/v1/product/insert")
    public ResponseEntity<?> insertProducts(@RequestPart("product") ProductDto productDto,
                                            @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                            Authentication authentication) throws IOException {
        String username = authentication.getName();
        Long productId = productService.insertProduct(productDto, files, username);
        log.info("상품등록 = {}", productId);
        return ResponseEntity.ok(Map.of("productId", productId));
    }

    //페이지 처리
    @GetMapping("/api/v1/product/list")
    public ResponseEntity<ProductPageDto> getPageProductList(@RequestParam(value = "page", defaultValue = "1")int page,
                                                             @RequestParam(value = "pageSize", defaultValue = "6") int pageSize) {
        ProductPageDto productPage = productService.getPagedProducts(page, pageSize);
        return ResponseEntity.ok(productPage);
    }

    // 제품 상세
    @GetMapping("/api/v1/product/detail/{productId}")
    public ResponseEntity<ProductDetailDto> getProductDetail(@PathVariable int productId) {
        ProductDetailDto productByNo = productService.getProductByNo(productId);
        return ResponseEntity.ok(productByNo);
    }

    // 제품 수정
    @PutMapping("/api/v1/product/update/{productId}")
    public ResponseEntity<Long> updateProduct(
            @PathVariable Long productId,
            @RequestPart("product") String productJson,
            @RequestPart(value = "files", required = false) MultipartFile file
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductUpdateDto productUpdateDto = objectMapper.readValue(productJson, ProductUpdateDto.class);
        // 업데이트 로직 수행
        Long updateProductId = productService.updateProduct(productId, productUpdateDto, file);

        log.info("updateProductId: {}", file);
        return ResponseEntity.ok(updateProductId);
    }


}
