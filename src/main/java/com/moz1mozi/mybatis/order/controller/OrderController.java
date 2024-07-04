package com.moz1mozi.mybatis.order.controller;

import com.moz1mozi.mybatis.cart.service.CartService;
import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.service.ShippingAddressService;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ShippingAddressService shippingAddressService;

    @GetMapping("/order/detail")
    public String showOrderPage(Model model, Principal principal) {
        MemberDto member = memberService.findByUsername(principal.getName());
        List<Long> addressId = memberService.getMemberAddress(member.getMemberId());

        log.info("RESULT: {}", addressId);
//        log.info("addressId : {}", addressId);
        model.addAttribute("loggedUser", member);
        model.addAttribute("addressId", addressId);
        return "order/detail";
    }

    @PostMapping("/order/detail")
    public String processOrderDetail(
            @RequestParam(value = "memberId") Long memberId,
            @RequestParam(value = "productId", required = false) String[] productIds,
            @RequestParam(value = "productName", required = false) String[] productNames,
            @RequestParam(value = "quantity", required = false) String[] quantities,
            @RequestParam(value = "price", required = false) String[] prices,
            Model model) {
        ShippingAddressDto address = shippingAddressService.getDefaultAddress(memberId);
        model.addAttribute("productIds", productIds);
        model.addAttribute("productNames", productNames);
        model.addAttribute("quantities", quantities);
        model.addAttribute("prices", prices);

        model.addAttribute("address", address);
        return "order/detail";
    }



}
