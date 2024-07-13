package com.moz1mozi.mybatis.category.service;

import com.moz1mozi.mybatis.category.mapper.CategoryMapper;
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
        List<CategoryDto> allCategories = categoryMapper.selectAllCategories();

        if (allCategories == null || allCategories.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, CategoryDto> categoryMap = new HashMap<>();
        List<CategoryDto> rootCategories = new ArrayList<>();
        allCategories.forEach(category -> categoryMap.put(category.getCateId(), category));

        allCategories.forEach(category -> {
            if (category.getParentCateId() == null) {
                rootCategories.add(category);
            } else {
                CategoryDto parent = categoryMap.get(category.getParentCateId());
                if (parent != null) {
                    parent.getSubCategories().add(category);
                }
            }
        });
        return rootCategories;
    }
}
