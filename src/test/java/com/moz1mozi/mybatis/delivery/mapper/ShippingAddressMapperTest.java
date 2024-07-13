package com.moz1mozi.mybatis.delivery.mapper;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ShippingAddressMapperTest {

    @Autowired
    ShippingAddressMapper shippingAddressMapper;


    @Test
    void 배송지_목록조회() {
        long userId = 12L;
        List<ShippingAddressDto> result = shippingAddressMapper.getShippingAddressByMemberId(userId);
        assertThat(result.get(0).getRecipientName()).isEqualTo("혬찌");
        assertThat(result.get(1).getRecipientName()).isEqualTo("엄복동");
        assertThat(result.get(0).getMobile()).isEqualTo("01012345678");
    }

    @Test
    void 배송지_단일조회() {
        long addressId = 2L;
        ShippingAddressDto result = shippingAddressMapper.getShippingAddressById(addressId);
        assertNotNull(result);
        log.info("result: {}", result.getRecipientName());
    }

    @Test
    void 배송지_수정() {
        ShippingAddressDto shippingAddressDto = ShippingAddressDto.builder()
                .addressId(16L)
                .mobile("01012345678")
                .zipcode("12345")
                .recipientName("히모지")
                .streetaddress("한강로")
                .detailaddress("카카오빌딩")
                .defaultAddress("Y")
                .build();
        shippingAddressMapper.updateShippingAddressById(shippingAddressDto);
    }

//    @Test
//    void 배송지_삭제() {
//        long addressId = 7L;
//        shippingAddressMapper.deleteShippingAddressById(addressId);
//    }
}