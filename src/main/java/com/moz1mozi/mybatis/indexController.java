package com.moz1mozi.mybatis;

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

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        if(principal != null) {
            String loggedUser = principal.getName();
            model.addAttribute("loggedUser", loggedUser);
        }

        return "index";
    }

    @GetMapping("/admin")
    public String testAdmin() {
        return "admin";
    }
}
