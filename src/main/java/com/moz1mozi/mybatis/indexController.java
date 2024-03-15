package com.moz1mozi.mybatis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class indexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
