package com.moz1mozi.mybatis.category.mapper;

import com.moz1mozi.mybatis.category.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryDto> selectAllCategories();
}
