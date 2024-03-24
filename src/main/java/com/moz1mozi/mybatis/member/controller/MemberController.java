package com.moz1mozi.mybatis.member.controller;

import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

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

            model.addAttribute("member", member);
        return "member/info";
    }
}
