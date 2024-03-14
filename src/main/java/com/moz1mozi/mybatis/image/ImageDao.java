package com.moz1mozi.mybatis.image;

import com.moz1mozi.mybatis.product.ProductDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageDao {

    //이미지 등록
    void insertProductImage(ImageDto imageDto);

}
