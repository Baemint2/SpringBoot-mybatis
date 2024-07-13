package com.moz1mozi.mybatis.delivery.service;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.dto.UpdateAddressDto;
import com.moz1mozi.mybatis.delivery.mapper.ShippingAddressMapper;
import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    private UserMapper userMapper;


    @Test
    void 배송지_등록() {
        long addressId = 1L;
        String username = "mary";
        UserDto user = memberService.findByUsername(username);
        log.info("userId: {}", user.getUserId());
        ShippingAddressDto addressDto = ShippingAddressDto.builder()
                .addressId(addressId)
                .userId(user.getUserId())
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
        UserDto name = memberService.findByUsername(username);
        List<ShippingAddressDto> result = shippingAddressService.getAllAddresses(name.getUserId());
        assertThat(result.get(0).getRecipientName()).isEqualTo("혬찌");
    }

    @Test
    void 배송지_수정() {
        Long addressId = 17L;
        UpdateAddressDto shippingAddressDto = UpdateAddressDto.builder()
                .mobile("01012345678")
                .zipcode("12345")
                .recipientName("히모지")
                .streetaddress("한강로")
                .detailaddress("카카오빌딩")
                .defaultAddress("Y")
                .build();

        shippingAddressService.updateAddress(addressId, shippingAddressDto);

    }

}