package com.moz1mozi.mybatis.order.controller;

import com.moz1mozi.mybatis.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CartService cartService;

    @GetMapping("/order/detail")
    public String showOrderPage( Model model) {
        return "order/detail";
    }

    @PostMapping("/order/detail")
    public String showOrderPage() {
        return "order/detail";
    }


}
