package com.moz1mozi.mybatis.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdateDto {

    private Long prodId;
    private Integer adjustment;
    @JsonProperty("isIncrease")
    private boolean isIncrease;

}
