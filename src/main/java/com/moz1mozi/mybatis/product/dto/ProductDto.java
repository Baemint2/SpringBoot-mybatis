package com.moz1mozi.mybatis.product.dto;

import com.moz1mozi.mybatis.image.dto.ImageDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
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
    @NotBlank(message = "상품 이름은 필수항목입니다.")
    private String prodName;
    @NotBlank(message = "상품 설명은 필수항목입니다.")
    private String description;
    @Min(value = 1000, message = "가격은 1000원 이상이어야합니다.")
    private int prodPrice;
    @Min(value = 1, message = "재고는 1개 이상이어야합니다.")
    private int stockQuantity;
    private Long categoryId;
    private Date createdAt;
    private Date modifiedAt;
    private List<ImageDto> imageDtoList;
    private String storedUrl;

    public void update(ProductUpdateDto updateProductDto) {
        this.productId = updateProductDto.getProductId();
        this.prodName = updateProductDto.getProdName();
        this.description = updateProductDto.getDescription();
        this.prodPrice = updateProductDto.getProdPrice();
        this.stockQuantity = updateProductDto.getStockQuantity();
        this.categoryId = updateProductDto.getCategoryId();
        this.modifiedAt = Date.from(Instant.now());
        this.imageDtoList =  updateProductDto.getImageDtoList();
        this.storedUrl = updateProductDto.getStoredUrl();
    }

}
