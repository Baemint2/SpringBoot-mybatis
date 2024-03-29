package com.moz1mozi.mybatis.category.service;

import com.moz1mozi.mybatis.category.dao.CategoryMapper;
import com.moz1mozi.mybatis.category.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;

    @Transactional
    public List<CategoryDto> getHierarchicalCategories() {
        log.info("Retrieving all categories from the database.");
        List<CategoryDto> allCategories = categoryMapper.selectAllCategories();

        if (allCategories == null || allCategories.isEmpty()) {
            log.info("No categories found in the database.");
            return Collections.emptyList();
        }

        log.info("Retrieved {} categories.", allCategories.size());

        Map<Long, CategoryDto> categoryMap = new HashMap<>();
        List<CategoryDto> rootCategories = new ArrayList<>();
        allCategories.forEach(category -> categoryMap.put(category.getCategoryId(), category));



        log.info("categoryMap : {}" , categoryMap);
        allCategories.forEach(category -> {
            if (category.getParentId() == null) {
                rootCategories.add(category);
            } else {
                CategoryDto parent = categoryMap.get(category.getParentId());
                if (parent != null) {
                    parent.getSubCategories().add(category);
                } else {
                    log.warn("Parent category ID: {} not found for category: {} (ID: {})", category.getParentId(), category.getCategoryName(), category.getCategoryId());
                }
            }
            log.info("rootCategories: {}", rootCategories);
        });
        return rootCategories;
    }
}
