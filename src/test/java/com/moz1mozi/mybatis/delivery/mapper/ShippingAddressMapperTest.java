package com.moz1mozi.mybatis.delivery.mapper;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        assertThat(result.get(0).getSaRecipientName()).isEqualTo("모지히");
        assertThat(result.get(1).getSaRecipientName()).isEqualTo("배영욱");
        assertThat(result.get(0).getSaMobile()).isEqualTo("01012345679");
    }

    @Test
    void 배송지_단일조회() {
        long addressId = 15L;
        ShippingAddressDto result = shippingAddressMapper.getShippingAddressById(addressId);
        assertNotNull(result);
        log.info("result: {}", result.getSaRecipientName());
    }

    @Test
    void 배송지_수정() {
        ShippingAddressDto shippingAddressDto = ShippingAddressDto.builder()
                .addressId(15L)
                .saMobile("01012345678")
                .saZipcode("12345")
                .saRecipientName("히모지")
                .saStreet("한강로")
                .saDetail("카카오빌딩")
                .saDefault("Y")
                .build();
        shippingAddressMapper.updateShippingAddressById(shippingAddressDto);
    }

    @Test
    @Transactional
    void 배송지_삭제() {
        long addressId = 23L;
        shippingAddressMapper.deleteShippingAddressById(addressId);
    }
}