package com.moz1mozi.mybatis.delivery.service;

import com.moz1mozi.mybatis.delivery.dto.AddressUpdateDto;
import com.moz1mozi.mybatis.delivery.mapper.ShippingAddressMapper;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShippingAddressService {

    private final ShippingAddressMapper shippingAddressMapper;
    private final MemberMapper memberMapper;


    @Transactional
    public void updateAddress(String username, AddressUpdateDto address) {
        Long memberId = memberMapper.findByMemberIdByUsername(username);
        if(memberId == null) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
        shippingAddressMapper.updateAddress(memberId, address);
    }
}
