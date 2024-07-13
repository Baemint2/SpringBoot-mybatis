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
    private String saRecipientName;
    private String saMobile;
    private String saZipcode;
    private String saStreet;
    private String saDetail;
    private String saDefault;
    private Date createDt;
    private Date modifiedDt;

    public void updateAddress(UpdateAddressDto updateAddressDto) {
        this.saRecipientName = updateAddressDto.getSaRecipientName();
        this.saMobile = updateAddressDto.getSaMobile();
        this.saZipcode = updateAddressDto.getSaZipcode();
        this.saDetail = updateAddressDto.getSaDetail();
        this.saStreet = updateAddressDto.getSaStreet();
        this.saDefault = updateAddressDto.getSaDefault();
    }
}
