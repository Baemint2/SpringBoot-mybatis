package com.moz1mozi.mybatis.delivery.service;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.mapper.ShippingAddressMapper;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ShippingAddressServiceTest {

    @Autowired
    ShippingAddressService shippingAddressService;

    @Autowired
    ShippingAddressMapper shippingAddressMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberMapper memberMapper;


    @Test
    void 배송지_등록() {
        long addressId = 1L;
        String username = "mary";
        MemberDto user = memberService.findByUsername(username);
        log.info("memberId: {}", user.getMemberId());
        ShippingAddressDto addressDto = ShippingAddressDto.builder()
                .addressId(addressId)
                .memberId(user.getMemberId())
                .recipientName("김말이")
                .mobile("01012345678")
                .zipcode("04006")
                .streetaddress("판교로 166-3")
                .detailaddress("4층")
                .build();


        shippingAddressService.addAddress(username, addressDto);
    }

    @Test
    void 배송지_조회() {
        String username = "mary";
        MemberDto name = memberService.findByUsername(username);
        List<ShippingAddressDto> result = shippingAddressService.getAddress(name.getMemberId());
        assertThat(result.get(0).getRecipientName()).isEqualTo("혬찌");
    }

}