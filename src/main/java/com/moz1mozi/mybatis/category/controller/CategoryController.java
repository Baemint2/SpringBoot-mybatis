package com.moz1mozi.mybatis.category.controller;

import com.moz1mozi.mybatis.category.dto.CategoryDto;
import com.moz1mozi.mybatis.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String categories() {
        return "categories";
    }

    @GetMapping("/api/v1/categories")
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<CategoryDto> categories = categoryService.getHierarchicalCategories();
        log.info("categories : {}", categories);
        return ResponseEntity.ok(categories);
    }
}
