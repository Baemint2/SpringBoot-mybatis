package com.moz1mozi.mybatis.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CategoryDto {
    private Long cateId;
    private String cateName;
    private Long parentCateId;
    private Date createdAt;
    private Date modifiedAt;
    private List<CategoryDto> subCategories;

    public List<CategoryDto> getSubCategories() {
        if(this.subCategories == null) {
            this.subCategories = new ArrayList<>();
        }
        return this.subCategories;
    }
}
