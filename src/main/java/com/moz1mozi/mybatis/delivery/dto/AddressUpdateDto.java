package com.moz1mozi.mybatis.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AddressUpdateDto {
    private String zipcode;
    private String streetAddress;
    private String detailAddress;
}
