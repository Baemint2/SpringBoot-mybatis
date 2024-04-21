package com.moz1mozi.mybatis.delivery.dto;


import lombok.*;

import java.util.Date;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressDto {
    private long addressId;
    private long memberId;
    private String recipientName;
    private String mobile;
    private String zipcode;
    private String streetaddress;
    private String detailaddress;
    private String defaultAddress;
    private Date createDt;
    private Date modifiedDt;
}
