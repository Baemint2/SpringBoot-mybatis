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
    private long prodId;
    private long sellerId;
    @NotBlank(message = "상품 이름은 필수항목입니다.")
    private String prodName;
    @NotBlank(message = "상품 설명은 필수항목입니다.")
    private String prodDescription;
    @Min(value = 1000, message = "가격은 1000원 이상이어야합니다.")
    private int prodPrice;
    @Min(value = 1, message = "재고는 1개 이상이어야합니다.")
    private int prodStockQuantity;
    private Long cateId;
    private Date createdAt;
    private Date modifiedAt;
    private List<ImageDto> imageDtoList;
    private String storedUrl;

    public void update(ProductUpdateDto updateProductDto) {
        this.prodId = updateProductDto.getProdId();
        this.prodName = updateProductDto.getProdName();
        this.prodDescription = updateProductDto.getProdDescription();
        this.prodPrice = updateProductDto.getProdPrice();
        this.prodStockQuantity = updateProductDto.getProdStockQuantity();
        this.cateId = updateProductDto.getCateId();
        this.modifiedAt = Date.from(Instant.now());
        this.imageDtoList =  updateProductDto.getImageDtoList();
        this.storedUrl = updateProductDto.getStoredUrl();
    }

}
