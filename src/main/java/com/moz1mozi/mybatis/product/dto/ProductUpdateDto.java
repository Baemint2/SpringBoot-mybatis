package com.moz1mozi.mybatis.product.dto;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductUpdateDto {
    private Long productId;
    private String prodName;
    private String description;
    private int prodPrice;
    private int stockQuantity;
    private Date modifiedAt;
    private List<ImageDto> imageDtoList;
}
