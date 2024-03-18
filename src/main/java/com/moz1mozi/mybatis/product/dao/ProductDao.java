package com.moz1mozi.mybatis.product.dao;

import com.moz1mozi.mybatis.product.dto.ProductDto;
import com.moz1mozi.mybatis.product.dto.ProductListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductDao {

    //상품 등록
    Long insertProduct(ProductDto productDto);

    //상품 삭제
    int deleteProduct(Long prodId);

    Long findAllWithImage(ProductDto productDto);

    List<ProductListDto> findAllProducts();
}
