package com.moz1mozi.mybatis.product.dto;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    private long productId;
    private long sellerId;
    private String prodName;
    private String description;
    private int prodPrice;
    private int stockQuantity;
    private Date createdAt;
    private Date modifiedAt;
    private List<ImageDto> imageDtoList;
}
