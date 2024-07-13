package com.moz1mozi.mybatis.delivery.dto;


import lombok.*;

import java.util.Date;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressDto {
    private long addressId;
    private long userId;
    private String recipientName;
    private String mobile;
    private String zipcode;
    private String streetaddress;
    private String detailaddress;
    private String defaultAddress;
    private Date createDt;
    private Date modifiedDt;

    public void updateAddress(UpdateAddressDto updateAddressDto) {
        this.recipientName = updateAddressDto.getRecipientName();
        this.mobile = updateAddressDto.getMobile();
        this.zipcode = updateAddressDto.getZipcode();
        this.detailaddress = updateAddressDto.getDetailaddress();
        this.streetaddress = updateAddressDto.getStreetaddress();
        this.defaultAddress = updateAddressDto.getDefaultAddress();
    }
}
