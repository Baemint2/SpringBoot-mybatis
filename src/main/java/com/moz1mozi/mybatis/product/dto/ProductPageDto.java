package com.moz1mozi.mybatis.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPageDto {
    private List<ProductDetailDto> products;
    private int currentPage;
    private int totalPages;
    private long totalProducts;
    private int pageSize;
    private Long categoryId;


}
