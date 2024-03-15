package com.moz1mozi.mybatis.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/user")
public class MemberController {

    @GetMapping("/signup")
    public String signup() {
        return "user/signup-form";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login-form";
    }
}
