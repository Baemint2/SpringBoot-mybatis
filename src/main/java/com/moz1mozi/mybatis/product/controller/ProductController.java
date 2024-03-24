package com.moz1mozi.mybatis.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.product.dto.*;
import com.moz1mozi.mybatis.product.service.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final MemberService memberService;
    private final Validator validator;

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
        List<ProductDetailDto> productByNo = productService.getProductByNo(productId);
        model.addAttribute("productByNo", productByNo);

        return "product/product-detail";
    }

    @GetMapping("/product/modify/{productId}")
    public String productModify(@PathVariable int productId,
                                Model model) {
        List<ProductDetailDto> productByNo = productService.getProductByNo(productId);
        model.addAttribute("productByNo", productByNo);

        return "product/product-modify";
    }


    //상품 등록
    @PostMapping("/api/v1/product/insert")
    public ResponseEntity<?> insertProducts(@Valid @RequestPart("product") ProductDto productDto,
                                            @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                            Authentication authentication) throws IOException {
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","상품 이미지는 필수입니다."));
        }
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
    public ResponseEntity<List<ProductDetailDto>> getProductDetail(@PathVariable int productId) {
        List<ProductDetailDto> productByNo = productService.getProductByNo(productId);
        return ResponseEntity.ok(productByNo);
    }

    // 제품 수정
    @PutMapping("/api/v1/product/update/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestPart("product") String productJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductUpdateDto productUpdateDto = objectMapper.readValue(productJson, ProductUpdateDto.class);

        Set<ConstraintViolation<ProductUpdateDto>> violations = validator.validate(productUpdateDto);
        if(!violations.isEmpty()) {
            Map<String, String> validationErrors = new HashMap<>();
            for (ConstraintViolation<ProductUpdateDto> violation : violations) {
                validationErrors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return ResponseEntity.badRequest().body(validationErrors);
        }
        // 업데이트 로직 수행
        Long updateProductId = productService.updateProduct(productId, productUpdateDto, files);

        return ResponseEntity.ok(updateProductId);
    }

    // 상품 삭제
    @ResponseBody
    @DeleteMapping("/api/v1/product/remove/{productId}")
    public Long removeProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return productId;
    }

    //상품 검색
//    @GetMapping("/api/v1/product/search")
//    public CompletableFuture<ResponseEntity<Page<ProductDetailDto>>> searchProduct(@RequestParam(required = false) String prodName,
//                                                                                   @RequestParam(required = false) String nickname,
//                                                                                   @RequestParam(required = false) Integer startPrice,
//                                                                                   @RequestParam(required = false) Integer endPrice,
//                                                                                   @RequestParam(value = "page", defaultValue = "1")int page,
//                                                                                   @RequestParam(value = "pageSize", defaultValue = "6") int pageSize) {
//        return productService.searchProductAsync(prodName, nickname, startPrice, endPrice)
//                .thenApply(ResponseEntity::ok);
//    }

}
