package com.moz1mozi.mybatis.delivery.mapper;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ShippingAddressMapperTest {

    @Autowired
    ShippingAddressMapper shippingAddressMapper;


    @Test
    void 배송지_조회() {
        long memberId = 12L;
        List<ShippingAddressDto> result = shippingAddressMapper.getShippingAddressByMemberId(memberId);
        assertThat(result.get(0).getRecipientName()).isEqualTo("혬찌");
        assertThat(result.get(1).getRecipientName()).isEqualTo("엄복동");
        assertThat(result.get(0).getMobile()).isEqualTo("01012345678");

    }
}