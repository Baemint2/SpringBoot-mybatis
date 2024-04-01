package com.moz1mozi.mybatis.order.controller;

import com.moz1mozi.mybatis.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final CartService cartService;

//    public String showOrderPage(@RequestParam List<Long> carItemIs, Model model) {


}
