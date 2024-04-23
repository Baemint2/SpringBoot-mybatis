package com.moz1mozi.mybatis.delivery.controller;

import com.moz1mozi.mybatis.delivery.dto.AddressUpdateDto;
import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.service.ShippingAddressService;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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

    //배송지 등록
    @PostMapping("/api/v1/address/insert")
    public ResponseEntity<?> insertAddress(@RequestBody ShippingAddressDto shippingAddressDto, Principal principal) {
        String name = principal.getName();
        String username = memberService.findByUsername(name).getUsername();
        shippingAddressService.addAddress(username, shippingAddressDto);
        return ResponseEntity.ok().body(Map.of("message", "배송지 등록이 성공적으로 되었습니다.", "address", "shippingAddressDto"));
    }

    //배송지 조회
    @GetMapping("/api/v1/address/{memberId}/addresses")
    public ResponseEntity<?> getAddress(@PathVariable Long memberId) {
        Long member = memberService.getMemberId(memberId);
        List<ShippingAddressDto> addresses = shippingAddressService.getAddress(member);
        return ResponseEntity.ok(addresses);
    }
}
