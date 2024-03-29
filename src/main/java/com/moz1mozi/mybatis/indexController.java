package com.moz1mozi.mybatis;

import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping
@Controller
@Slf4j
@RequiredArgsConstructor
public class indexController {
    private final MemberService memberService;

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        if(principal != null) {
            String username = principal.getName();
            MemberDto loggedUser = memberService.findByUsername(username);
            model.addAttribute("loggedUser", loggedUser);
        }

        return "index";
    }

    @GetMapping("/admin")
    public String testAdmin() {
        return "admin";
    }
}
