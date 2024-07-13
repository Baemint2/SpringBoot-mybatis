package com.moz1mozi.mybatis.delivery.service;

import com.moz1mozi.mybatis.delivery.dto.UpdateAddressDto;
import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.mapper.ShippingAddressMapper;
import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {

    private final ShippingAddressMapper shippingAddressMapper;
    private final UserService userService;


    @Transactional
    public void addAddress(String username, ShippingAddressDto shippingAddressDto) {
        Long userId = Optional.ofNullable(userService.getMemberIdByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));

        shippingAddressMapper.insertShippingAddress(userId, shippingAddressDto);
    }

    @Transactional(readOnly = true)
    public List<Long> getAddressIdByMemberId(Long userId) {
        return shippingAddressMapper.getAddressIdByMemberId(userId);
    }

    @Transactional(readOnly = true)
    public List<ShippingAddressDto> getAllAddresses(Long userId) {

        return Optional.ofNullable(userId)
                .map(shippingAddressMapper::getShippingAddressByMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public ShippingAddressDto getAddress(Long userId) {

        return Optional.ofNullable(shippingAddressMapper.getShippingAddressById(userId))
                    .orElseThrow(() -> new CustomException("addressNotFound","배송지가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public ShippingAddressDto getDefaultAddress(Long userId) {
        return Optional.ofNullable(shippingAddressMapper.getDefaultAddressByMemberId(userId))
                .orElseThrow(() -> new CustomException("addressNotFound","기본 배송지가 존재하지 않습니다."));
    }

    // 배송지 수정
    @Transactional
    public Long updateAddress(Long addressId, UpdateAddressDto updateAddressDto) {
        ShippingAddressDto existingAddress = Optional.ofNullable(shippingAddressMapper.getShippingAddressById(addressId))
                .orElseThrow(() -> new IllegalArgumentException("해당 주소가 없습니다."));

        existingAddress.updateAddress(updateAddressDto);

        shippingAddressMapper.updateShippingAddressById(existingAddress);

        return existingAddress.getAddressId();
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        shippingAddressMapper.deleteShippingAddressById(addressId);
    }
}
