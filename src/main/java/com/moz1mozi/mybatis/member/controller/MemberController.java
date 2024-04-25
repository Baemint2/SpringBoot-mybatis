package com.moz1mozi.mybatis.member.controller;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.service.ShippingAddressService;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final ShippingAddressService shippingAddressService;

    @GetMapping("/signup")
    public String signup() {
        return "member/signup-form";
    }

    @GetMapping("/login")
    public String login() {
        return "member/login-form";
    }

    @GetMapping("/info")
    public String info(Model model, Principal principal) {
        MemberDto member = memberService.findByUsername(principal.getName());
        List<Long> addressId = memberService.getMemberAddress(member.getMemberId());

        log.info("RESULT: {}", addressId);
//        log.info("addressId : {}", addressId);
        model.addAttribute("member", member);
        model.addAttribute("addressId", addressId);
        return "member/info";
    }
}
