package com.moz1mozi.mybatis.image.dao;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageDao {

    //이미지 등록
    void insertProductImage(ImageDto imageDto);

    //이미지 수정
    void updateProductImage(ImageDto imageDto);

    ImageDto findByProductId(Long prodId);
}
