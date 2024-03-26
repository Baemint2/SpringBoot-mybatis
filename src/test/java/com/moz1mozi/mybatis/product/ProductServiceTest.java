package com.moz1mozi.mybatis.product;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.member.dao.MemberDao;
import com.moz1mozi.mybatis.product.dao.ProductDao;
import com.moz1mozi.mybatis.product.dto.*;
import com.moz1mozi.mybatis.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ProductServiceTest {


    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private ImageService imageService;

    @Test
    void 상품등록테스트() {
        ProductDto productDto = ProductDto.builder()
                .prodName("모지화장품")
                .description("안녕하세요 첫 출시 해봤습니다.")
                .prodPrice(5000)
                .stockQuantity(5)
                .categoryId(10L)
                .createdAt(Date.from(Instant.now()))
                .build();

//        Long result = productService.insertProduct(productDto);
//        assertEquals(1, result);
    }

    @Test
    void 상품삭제() {
        int deleteCount = productService.deleteProduct(1L);
        assertEquals(1, deleteCount);
    }
    @Test
    public void testInsertProductWithImage() {
        ProductDto productDto = ProductDto.builder()
                .prodName("Test Product")
                .description("Test Description")
                .prodPrice(10000)
                .stockQuantity(10)
                .createdAt(new Date())
                .modifiedAt(new Date())
                .build();

        List<ImageDto> imageDtoList = Arrays.asList(
                new ImageDto(0, 0, "originalFileName1.jpg", "storedFileName1.jpg", "storedUrl1", null, null),
                new ImageDto(0, 0, "originalFileName2.jpg", "storedFileName2.jpg", "storedUrl2", null, null)
        );
//        Long productId = productService.insertProduct(productDto, imageDtoList);
        // 여기에서 필요한 경우 추가적인 검증 로직을 구현할 수 있습니다.
        // 예: 삽입된 상품 및 이미지 정보를 조회하여 예상된 결과와 일치하는지 확인
    }
    @Test
    void 상품리스트() {

        List<ProductListDto> products = productService.findAllProducts();
        log.info("products: {}", products);
        assertThat(products).isNotNull();
        assertThat(products.size()).isGreaterThan(0);

        assertThat(products.get(0).getProdName()).isEqualTo("모지화장품");
    }

    @Test
    void 상품조회_테스트_성공() {
        int productId = 18;
        List<ProductDetailDto> productByNo = productService.getProductByNo(productId);
        assertNotNull(productByNo);
    }


    @Test
    // 존재하지 않는 memberID로 조회
    void 상품조회_테스트_실패() {
        int productId = 99;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.getProductByNo(productId));
        String exceptionMessage = "해당 상품이 존재하지 않습니다.";
        String message = exception.getMessage();
        assertTrue(message.contains(exceptionMessage));
    }

    @Test
    @Rollback(value = false)
    void 상품정보수정_테스트() throws IOException {
        // 테스트를 위한 상품 ID
        Long prodId = 2L; // 실제 데이터베이스에 존재하는 상품 ID로 변경해야 합니다.

        // 수정할 상품 정보를 담은 DTO,
        ProductUpdateDto productUpdateDto = ProductUpdateDto.builder()
                .productId(prodId)
                .prodName("수수정된 상품")
                .description("진짜진짜 수정된")
                .prodPrice(20000)
                .stockQuantity(20)
                .build();

        byte[] content = "example file content".getBytes();
        List<MultipartFile> mockFile = List.of(new MockMultipartFile("file", "filename.txt", "text/plain", content));

        // 상품 수정 실행
        productService.updateProduct(prodId, productUpdateDto, mockFile);

        // 검증: 수정된 상품 정보 조회
        ProductDto updatedProduct = productDao.selectProductById(prodId);
        assertNotNull(updatedProduct);
        assertEquals("수수정된 상품", updatedProduct.getProdName());
        assertEquals("진짜진짜 수정된", updatedProduct.getDescription());
        assertEquals(20000, updatedProduct.getProdPrice());
        assertEquals(20, updatedProduct.getStockQuantity());
    }

    @Test
    void 상품검색_상품명_테스트() throws ExecutionException, InterruptedException {
        ProductSearchDto productSearchDto = ProductSearchDto.builder()
                .categoryId(6L)
                .page(1)  // 페이지 번호 지정
                .pageSize(10)  // 페이지 크기 지정
                .build();
        CompletableFuture<ProductPageDto> futureResults = productService.searchProductsWithPagingAsync(productSearchDto.getProdName(), productSearchDto.getNickname(), productSearchDto.getStartPrice(),
                                                                            productSearchDto.getEndPrice(), productSearchDto.getPage(), productSearchDto.getPageSize(), productSearchDto.getCategoryId());
        ProductPageDto results = futureResults.get();
        assertTrue(results.getTotalProducts() > 0);
    }

//    @Test
//    void 상품검색_상품명_실패() throws ExecutionException, InterruptedException {
//        ProductSearchDto productSearchDto = ProductSearchDto.builder()
//                .prodName("없음")
//                .build();
//        CompletableFuture<ProductPageDto> futureResults = productService.searchProductsWithPagingAsync(productSearchDto, 1, 10); // 페이지와 페이지 크기를 명시적으로 지정
//        ProductPageDto results = futureResults.get();
//        assertEquals(0, results.getTotalProducts());
//    }
//
//    @Test
//    void 상품검색_판매자_테스트() throws ExecutionException, InterruptedException {
//        ProductSearchDto productSearchDto = ProductSearchDto.builder()
//                .nickname("마리")
//                .build();
//        CompletableFuture<ProductPageDto> futureResults = productService.searchProductsWithPagingAsync(productSearchDto, 1, 10); // 페이지와 페이지 크기를 명시적으로 지정
//        ProductPageDto results = futureResults.get();
//        assertTrue(results.getTotalProducts() > 0);
//    }
//
//    @Test
//    void 상품검색_가격대_테스트() throws ExecutionException, InterruptedException {
//        ProductSearchDto productSearchDto = ProductSearchDto.builder()
//                .endPrice(10000)
//                .build();
//        CompletableFuture<ProductPageDto> futureResults = productService.searchProductsWithPagingAsync(productSearchDto, 1, 10); // 페이지와 페이지 크기를 명시적으로 지정
//        ProductPageDto results = futureResults.get();
//        assertTrue(results.getTotalProducts() > 0);
//    }
//
//    @Test
//    void 상품검색_상품명_가격대_테스트() throws ExecutionException, InterruptedException {
//        ProductSearchDto productSearchDto = ProductSearchDto.builder()
//                .prodName("상")
//                .endPrice(20000)
//                .build();
//        CompletableFuture<ProductPageDto> futureResults = productService.searchProductsWithPagingAsync(productSearchDto, 1, 10); // 페이지와 페이지 크기를 명시적으로 지정
//        ProductPageDto results = futureResults.get();
//        assertTrue(results.getTotalProducts() > 0);
//    }
//
//    @Test
//    void 전체상품검색() throws ExecutionException, InterruptedException {
//        ProductSearchDto productSearchDto = ProductSearchDto.builder()
//                .build();
//        CompletableFuture<ProductPageDto> futureResults = productService.searchProductsWithPagingAsync(productSearchDto, 1, 10); // 페이지와 페이지 크기를 명시적으로 지정
//        ProductPageDto results = futureResults.get();
//        assertTrue(results.getTotalProducts() > 0);
//    }
}
