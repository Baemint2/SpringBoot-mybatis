package com.moz1mozi.mybatis.delivery.service;

import com.moz1mozi.mybatis.delivery.dto.UpdateAddressDto;
import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.mapper.ShippingAddressMapper;
import com.moz1mozi.mybatis.exception.CustomException;
import com.moz1mozi.mybatis.member.service.MemberService;
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
    private final MemberService memberService;


    @Transactional
    public void addAddress(String username, ShippingAddressDto shippingAddressDto) {
        Long memberId = Optional.ofNullable(memberService.getMemberIdByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));

        shippingAddressMapper.insertShippingAddress(memberId, shippingAddressDto);
    }

    @Transactional(readOnly = true)
    public List<Long> getAddressIdByMemberId(Long memberId) {
        return shippingAddressMapper.getAddressIdByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<ShippingAddressDto> getAllAddresses(Long memberId) {

        return Optional.ofNullable(memberId)
                .map(shippingAddressMapper::getShippingAddressByMemberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public ShippingAddressDto getAddress(Long addressId) {

        return Optional.ofNullable(shippingAddressMapper.getShippingAddressById(addressId))
                    .orElseThrow(() -> new CustomException("addressNotFound","배송지가 존재하지 않습니다."));
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
