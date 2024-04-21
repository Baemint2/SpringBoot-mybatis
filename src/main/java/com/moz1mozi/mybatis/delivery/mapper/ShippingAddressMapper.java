package com.moz1mozi.mybatis.delivery.mapper;

import com.moz1mozi.mybatis.delivery.dto.AddressUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShippingAddressMapper {

    void updateAddress(@Param("memberId")Long memberId, @Param("address") AddressUpdateDto addressUpdateDto);

}
