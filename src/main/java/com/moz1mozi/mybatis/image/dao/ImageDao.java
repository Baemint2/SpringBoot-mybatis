package com.moz1mozi.mybatis.image.dao;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageDao {

    //이미지 등록
    void insertProductImage(ImageDto imageDto);

}
