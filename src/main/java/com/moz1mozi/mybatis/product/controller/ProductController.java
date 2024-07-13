package com.moz1mozi.mybatis.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moz1mozi.mybatis.common.exception.OutOfStockException;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.UserService;
import com.moz1mozi.mybatis.product.dto.*;
import com.moz1mozi.mybatis.product.service.ProductService;
import com.moz1mozi.mybatis.wishlist.service.WishListService;
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
    private final UserService userService;
    private final WishListService wishListService;
    private final Validator validator;

    @GetMapping("/product")
    public String products(Model model, Principal principal) {
        String username = principal.getName();
        UserDto loggedUser = userService.findByUsername(username);
        model.addAttribute("loggedUser", loggedUser);
        return "product/product-insert";
    }

    @GetMapping("/product/detail/{prodId}")
    public String productDetail(@PathVariable Long prodId,
                                Model model,
                                Principal principal) {
        if(principal != null) {
            String currentUsername = principal.getName();
            UserDto loggedUser = userService.findByUsername(currentUsername);
            model.addAttribute("loggedUser", loggedUser);
            Long userId = loggedUser.getUserId();
            boolean isLiked = wishListService.isLiked(userId, prodId);
            model.addAttribute("isLiked", isLiked);


            List<ProductDetailDto> productByNo = productService.getProductByNo(prodId);
            model.addAttribute("productByNo", productByNo);


            if (!productByNo.isEmpty()) {
                boolean isOwner = productService.isUserSellerOfProduct(currentUsername, prodId);
                boolean isAdmin = productService.isCurrentUserAdmin();
                model.addAttribute("isOwner", isOwner);
                model.addAttribute("isAdmin", isAdmin);
                log.info("현재 사용자 = {}", isOwner);
            }
        } else {
            model.addAttribute("isLiked", false);
            List<ProductDetailDto> productByNo = productService.getProductByNo(prodId);
            model.addAttribute("productByNo", productByNo);
            model.addAttribute("isOwner", false);
            model.addAttribute("isAdmin", false);
        }
        return "product/product-detail";
    }

    @GetMapping("/product/modify/{prodId}")
    public String productModify(@PathVariable Long prodId,
                                Model model) {
        List<ProductDetailDto> productByNo = productService.getProductByNo(prodId);
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
        log.info("카테고리 ID : {}", productDto.getCateId());
        String username = authentication.getName();
        Long prodId = productService.insertProduct(productDto, files, username);
        log.info("상품등록 = {}", prodId);
        return ResponseEntity.ok(Map.of("prodId", prodId));
    }

    // 제품 상세
    @GetMapping("/api/v1/product/detail/{prodId}")
    public ResponseEntity<List<ProductDetailDto>> getProductDetail(@PathVariable Long prodId) {
        List<ProductDetailDto> productByNo = productService.getProductByNo(prodId);
        return ResponseEntity.ok(productByNo);
    }

    // 제품 수정
    @PutMapping("/api/v1/product/update/{prodId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long prodId,
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
        Long updateprodId = productService.updateProduct(prodId, productUpdateDto, files);
        log.info("result : {}", updateprodId);
        return ResponseEntity.ok(updateprodId);
    }

    // 상품 삭제
    @DeleteMapping("/api/v1/product/remove/{prodId}")
    public ResponseEntity<Long> removeProduct(@PathVariable Long prodId) {
        productService.deleteProduct(prodId);
        return ResponseEntity.ok(prodId);
    }

    //    상품 검색
    @GetMapping("/api/v1/product/search")
    public CompletableFuture<ResponseEntity<ProductPageDto>> searchProduct(
            @RequestParam(required = false) String prodName,
            @RequestParam(required = false) String userNickname,
            @RequestParam(required = false) Integer startPrice,
            @RequestParam(required = false) Integer endPrice,
            @RequestParam(required = false) Long cateId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "6") int pageSize) {

        log.info("설정된 카테고리값 = {}", cateId);
        return productService.searchProductsWithPagingAsync(prodName, userNickname, startPrice, endPrice, page, pageSize, cateId)
                .thenApply(ResponseEntity::ok);
    }

    // 남은 재고
    @GetMapping("/api/v1/product/{prodId}/stock")
    public ResponseEntity<Integer> getStockByProdId(@PathVariable Long prodId) {
        int stock = productService.getStockByProductId(prodId);
        return ResponseEntity.ok(stock);
    }

    // 수량 업데이트
    @PutMapping("/api/v1/product/stock/update")
    public ResponseEntity<?> adjustStock(@RequestBody StockUpdateDto stockUpdateDto) {
        log.info("{} {} {}", stockUpdateDto.getProdId(), stockUpdateDto.getAdjustment(), stockUpdateDto.isIncrease());
        try {
            productService.adjustStockQuantity(stockUpdateDto.getProdId(), stockUpdateDto.getAdjustment(), stockUpdateDto.isIncrease());
            return ResponseEntity.ok().build();
        } catch (OutOfStockException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
