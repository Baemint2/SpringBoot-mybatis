package com.moz1mozi.mybatis.category.service;

import com.moz1mozi.mybatis.category.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    void 계층형쿼리테스트() {
        List<CategoryDto> categories = categoryService.getHierarchicalCategories();
        for (CategoryDto category : categories) {
            printCategory(category, "");
        }
    }

    private void printCategory(CategoryDto categoryDto, String indent) {
        System.out.println(indent + "- " + categoryDto.getCateName());
        List<CategoryDto> subCategories = categoryDto.getSubCategories();
        for (CategoryDto subCategory : subCategories) {
            printCategory(subCategory, indent + " ");
        }
    }
}