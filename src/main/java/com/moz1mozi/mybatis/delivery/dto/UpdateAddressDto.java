package com.moz1mozi.mybatis.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UpdateAddressDto {
    private String recipientName;
    private String mobile;
    private String zipcode;
    private String streetaddress;
    private String detailaddress;
    private String defaultAddress;
    private Date modifiedDt;
}
