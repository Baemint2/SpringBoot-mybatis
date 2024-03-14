package com.moz1mozi.mybatis.product;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDao {

    //상품 등록
    Long insertProduct(ProductDto productDto);

    //상품 삭제
    int deleteProduct(Long prodId);
}
