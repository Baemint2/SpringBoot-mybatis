package com.moz1mozi.mybatis.image.dao;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper {

    //이미지 등록
    int insertProductImage(List<ImageDto> imageDto);

    //이미지 수정
    int updateProductImage(List<ImageDto>  imageDto);

    List<ImageDto> findByProductId(Long prodId);
}
