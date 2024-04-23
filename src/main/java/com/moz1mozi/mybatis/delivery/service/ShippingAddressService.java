package com.moz1mozi.mybatis.delivery.service;

import com.moz1mozi.mybatis.delivery.dto.AddressUpdateDto;
import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.mapper.ShippingAddressMapper;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {

    private final ShippingAddressMapper shippingAddressMapper;
    private final MemberService memberService;


    @Transactional
    public void addAddress(String username, ShippingAddressDto shippingAddressDto) {
        Long memberId = memberService.getMemberIdByUsername(username);
        if(memberId == null) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
        shippingAddressMapper.insertShippingAddress(memberId, shippingAddressDto);
    }

    @Transactional(readOnly = true)
    public List<ShippingAddressDto> getAddress(Long memberId) {
        if(memberId == null) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
        return shippingAddressMapper.getShippingAddressByMemberId(memberId);
    }


//    @Transactional
//    public void updateAddress(String username, AddressUpdateDto address) {
//        Long memberId = memberMapper.findByMemberIdByUsername(username);
//        if(memberId == null) {
//            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
//        }
//        shippingAddressMapper.updateAddress(memberId, address);
//    }
}
