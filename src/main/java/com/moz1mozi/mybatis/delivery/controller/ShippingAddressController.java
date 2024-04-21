package com.moz1mozi.mybatis.delivery.controller;

import com.moz1mozi.mybatis.delivery.dto.AddressUpdateDto;
import com.moz1mozi.mybatis.delivery.service.ShippingAddressService;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;
    private final MemberService memberService;

    @GetMapping("/addressBook")
    public String addressBook(Model model) {
        return "addressBook";
    }


    @PutMapping("/updateAddress")
    public ResponseEntity<?> updateAddress(@RequestBody AddressUpdateDto address, Principal principal) {
        String username = principal.getName();
        shippingAddressService.updateAddress(username, address);
        return ResponseEntity.ok().body(Map.of("message", "주소가 성공적으로 업데이트 되었습니다.", "address", address));
    }
}
