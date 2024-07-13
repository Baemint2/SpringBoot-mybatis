package com.moz1mozi.mybatis.delivery.controller;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.dto.UpdateAddressDto;
import com.moz1mozi.mybatis.delivery.service.ShippingAddressService;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.MemberService;
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
    @PostMapping("/api/v1/address")
    public ResponseEntity<?> insertAddress(@RequestBody ShippingAddressDto shippingAddressDto, Principal principal) {
        String name = principal.getName();
        String username = memberService.findByUsername(name).getUserName();
        shippingAddressService.addAddress(username, shippingAddressDto);
        return ResponseEntity.ok().body(Map.of("message", "배송지 등록이 성공적으로 되었습니다.", "address", "shippingAddressDto"));
    }

    //배송지 목록 조회
    @GetMapping("/api/v1/address/{userId}/addresses")
    public ResponseEntity<List<ShippingAddressDto>> getAllAddresses(@PathVariable Long userId) {
        UserDto member = memberService.getMemberId(userId);
        List<ShippingAddressDto> addresses = shippingAddressService.getAllAddresses(member.getUserId());
        return ResponseEntity.ok(addresses);
    }

    //배송지 단일 조회
    @GetMapping("/api/v1/address/{addressId}")
    public ResponseEntity<ShippingAddressDto> getAddress(@PathVariable Long addressId) {
        ShippingAddressDto address = shippingAddressService.getAddress(addressId);
        return ResponseEntity.ok(address);
    }

    //배송지 수정
    @PutMapping("/api/v1/address/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long addressId, @RequestBody UpdateAddressDto updateAddressDto) {
        Long updateAddressId = shippingAddressService.updateAddress(addressId, updateAddressDto);
        log.info("result : {}", updateAddressId);
        return ResponseEntity.ok(updateAddressId);
    }


    //배송지 삭제
    @DeleteMapping("/api/v1/address/{addressId}")
    public ResponseEntity<Long> removeAddress(@PathVariable Long addressId) {
        shippingAddressService.deleteAddress(addressId);
        return ResponseEntity.ok(addressId);
    }
}
