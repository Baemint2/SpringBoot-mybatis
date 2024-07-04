package com.moz1mozi.mybatis.product.dao;

import com.moz1mozi.mybatis.product.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    //상품 등록
    Long insertProduct(ProductDto productDto);

    // 상품 수정
    Long updateProduct(ProductDto productDto);

    ProductDto selectProductById(Long prodId);
    //상품 삭제
    int deleteProduct(Long prodId);

    // 상품 상세 조회
    List<ProductDetailDto> getProductByNo(Long productId);

    //상품 목록 조회
    List<ProductListDto> findAllProducts();

    //상품 검색
    List<ProductDetailDto> findByCondition(@Param("searchDto") ProductSearchDto searchDto,
                                           @Param("pageSize") int pageSize,
                                           @Param("offset") int offset);
    //상품 검색 개수
    Long countByCondition(ProductSearchDto productSearchDto);

    // 판매자 권한 비교
    @Select("SELECT m.user_name FROM User M JOIN PRODUCT_T P ON M.user_id = P.SELLER_ID WHERE P.PRODUCT_ID = #{productId}")
    String findSellerUsernameByProductId(@Param("productId")Long productId);

    // 어드민 권한 비교
    @Select("SELECT user_role FROM User WHERE user_role = #{role} ")
    String findAdminByRole(@Param("role")String role);

    // 재고 업데이트
    void updateStockQuantity(StockUpdateDto stockUpdateDto);

    // 재고 확인
    int getStockByProductId(Long productId);

    // 실제 재고
    int getActualStockByProductId(Long productId);

    // 수량 증가
    void increaseStockQuantity(@Param("productId")Long productId, @Param("quantity") Integer quantity);

}
