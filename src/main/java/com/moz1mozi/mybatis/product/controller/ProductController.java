package com.moz1mozi.mybatis.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moz1mozi.mybatis.exception.OutOfStockException;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.product.dto.*;
import com.moz1mozi.mybatis.product.service.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        String currentUsername = principal.getName();

        List<ProductDetailDto> productByNo = productService.getProductByNo(productId);
        model.addAttribute("productByNo", productByNo);


        if (!productByNo.isEmpty()) {
            boolean isOwner = productService.isUserSellerOfProduct(currentUsername, productId);
            boolean isAdmin = productService.isCurrentUserAdmin();

            model.addAttribute("isOwner", isOwner);
            model.addAttribute("isAdmin", isAdmin);
            log.info("현재 사용자 = {}", isOwner);
        }

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
            return ResponseEntity.badRequest().body(Map.of("message", "상품 이미지는 필수입니다."));
        }
        log.info("카테고리 ID : {}", productDto.getCategoryId());
        String username = authentication.getName();
        Long productId = productService.insertProduct(productDto, files, username);
        log.info("상품등록 = {}", productId);
        return ResponseEntity.ok(Map.of("productId", productId));
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
        if (!violations.isEmpty()) {
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

    //    상품 검색
    @GetMapping("/api/v1/product/search")
    public CompletableFuture<ResponseEntity<ProductPageDto>> searchProduct(
            @RequestParam(required = false) String prodName,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Integer startPrice,
            @RequestParam(required = false) Integer endPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "6") int pageSize) {

        log.info("설정된 카테고리값 = {}", categoryId);
        return productService.searchProductsWithPagingAsync(prodName, nickname, startPrice, endPrice, page, pageSize, categoryId)
                .thenApply(ResponseEntity::ok);
    }

    // 남은 재고
    @GetMapping("/api/v1/product/{productId}/stock")
    public ResponseEntity<Integer> getStockByProductId(@PathVariable Long productId) {
        int stock = productService.getStockByProductId(productId);
        return ResponseEntity.ok(stock);
    }

    // 수량 업데이트
    @PostMapping("/api/v1/product/stock/update")
    public ResponseEntity<?> adjustStock(@RequestBody StockUpdateDto stockUpdateDto) {
        log.info("{} {} {}", stockUpdateDto.getProductId(), stockUpdateDto.getNewQuantity(), stockUpdateDto.isIncrease());
        try {
            productService.adjustStockQuantity(stockUpdateDto.getProductId(), stockUpdateDto.getNewQuantity(), stockUpdateDto.isIncrease());
            return ResponseEntity.ok().build();
        } catch (OutOfStockException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
