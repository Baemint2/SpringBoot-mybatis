package com.moz1mozi.mybatis.delivery.service;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.dto.UpdateAddressDto;
import com.moz1mozi.mybatis.delivery.mapper.ShippingAddressMapper;
import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    private UserService userService;
    @Autowired
    private UserMapper userMapper;


    @Test
    @Transactional
    void 배송지_등록() {
        long addressId = 24L;
        String username = "mary";
        UserDto user = userService.findByUsername(username);
        log.info("userId: {}", user.getUserId());
        ShippingAddressDto addressDto = ShippingAddressDto.builder()
                .addressId(addressId)
                .userId(user.getUserId())
                .saRecipientName("김말이")
                .saMobile("01012345678")
                .saZipcode("04006")
                .saStreet("판교로 166-3")
                .saDetail("4층")
                .build();


        shippingAddressService.addAddress(username, addressDto);
    }

    @Test
    void 배송지_조회() {
        String username = "mary";
        UserDto name = userService.findByUsername(username);
        List<ShippingAddressDto> result = shippingAddressService.getAllAddresses(name.getUserId());
        assertThat(result.get(0).getSaRecipientName()).isEqualTo("히모지");
    }

    @Test
    void 배송지_수정() {
        Long addressId = 15L;
        UpdateAddressDto shippingAddressDto = UpdateAddressDto.builder()
                .saMobile("01012345678")
                .saZipcode("12345")
                .saRecipientName("히모지")
                .saStreet("한강로")
                .saDetail("카카오빌딩")
                .saDefault("Y")
                .build();

        shippingAddressService.updateAddress(addressId, shippingAddressDto);

    }

}