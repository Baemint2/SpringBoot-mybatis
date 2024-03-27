package com.moz1mozi.mybatis.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdateDto {

    private Long productId;
    private Integer newQuantity;
    @JsonProperty("isIncrease")
    private boolean isIncrease;

}
