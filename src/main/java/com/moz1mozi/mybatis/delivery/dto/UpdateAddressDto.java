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
    private String saRecipientName;
    private String saMobile;
    private String saZipcode;
    private String saStreet;
    private String saDetail;
    private String saDefault;
    private Date modifiedDt;
}
