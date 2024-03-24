package com.moz1mozi.mybatis.product.dto;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "상품 이름은 필수항목입니다.")
    private String prodName;
    @NotBlank(message = "상품 설명은 필수항목입니다.")
    private String description;
    @Min(value = 1000, message = "가격은 1000원 이상이어야합니다.")
    private int prodPrice;
    @Min(value = 1, message = "재고는 1개 이상이어야합니다.")
    private int stockQuantity;
    private Date modifiedAt;
    private List<ImageDto> imageDtoList;
    private String storedUrl;
}
