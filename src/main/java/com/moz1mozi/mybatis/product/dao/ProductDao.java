package com.moz1mozi.mybatis.product.dao;

import com.moz1mozi.mybatis.product.dto.ProductDetailDto;
import com.moz1mozi.mybatis.product.dto.ProductDto;
import com.moz1mozi.mybatis.product.dto.ProductListDto;
import com.moz1mozi.mybatis.product.dto.ProductSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface ProductDao {

    //상품 등록
    Long insertProduct(ProductDto productDto);

    // 상품 수정
    Long updateProduct(ProductDto productDto);

    ProductDto selectProductById(Long prodId);
    //상품 삭제
    int deleteProduct(Long prodId);

    @Select("SELECT COUNT(*) FROM PRODUCT_T")
    Long countAllProducts();

    List<ProductDetailDto> getPagedProducts(@Param("size")int limit, @Param("offset")int offset);

    // 상품 상세 조회
    List<ProductDetailDto> getProductByNo(int productId);

    //상품 목록 조회
    List<ProductListDto> findAllProducts();

    //상품 검색
    List<ProductDetailDto> findByCondition(ProductSearchDto productSearchDto, int size, int pageSize);

    //상품 검색 개수
    Long countByCondition(ProductSearchDto productSearchDto);
}
