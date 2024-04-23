package com.moz1mozi.mybatis.delivery.mapper;

import com.moz1mozi.mybatis.delivery.dto.AddressUpdateDto;
import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingAddressMapper {

    void updateAddress(@Param("memberId")Long memberId, @Param("address") AddressUpdateDto addressUpdateDto);

    Long insertShippingAddress(@Param("memberId") Long memberId, @Param("address") ShippingAddressDto shippingAddressDto);

    List<ShippingAddressDto> getShippingAddressByMemberId(Long memberId);

}
