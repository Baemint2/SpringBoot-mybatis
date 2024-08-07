package com.moz1mozi.mybatis.delivery.mapper;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShippingAddressMapper {

    Long insertShippingAddress(@Param("userId") Long userId, @Param("address") ShippingAddressDto shippingAddressDto);

    // 배송지 목록 조회
    List<ShippingAddressDto> getShippingAddressByMemberId(Long userId);

    // 배송지 단일 조회
    ShippingAddressDto getShippingAddressById(Long addressId);

    // 기본 배송지 조회
    ShippingAddressDto getDefaultAddressByMemberId(Long userId);

    List<Long> getAddressIdByMemberId(Long userId);

    // 배송지 수정
    int updateShippingAddressById(ShippingAddressDto shippingAddressDto);

    // 배송지 삭제
    void deleteShippingAddressById(Long addressId);

}
