package com.moz1mozi.mybatis.user.controller;

import com.moz1mozi.mybatis.delivery.service.ShippingAddressService;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.UserSecurityService;
import com.moz1mozi.mybatis.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserSecurityService userSecurityService;
    private final ShippingAddressService shippingAddressService;

    @GetMapping("/signup")
    public String signup() {
        return "user/signup-form";
    }

    @GetMapping("/login")
    public String login() {
        return "user/login-form";
    }

    @GetMapping("/find/username")
    public String findUsername() {
        return "user/find-username";
    }

    @PostMapping("/find/username")
    public String findUsernamePost(@ModelAttribute UserDto userDto, BindingResult result,
                                   RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "user/find-username";
        }

        UserDto findUsername = userService.getEmail(userDto.getUserEmail());
        if (findUsername != null) {
            ra.addFlashAttribute("findUsername", findUsername.getUserName());
        } else {
            ra.addFlashAttribute("error", "해당 이메일로 가입된 아이디가 없습니다.");
        }
        return "redirect:/user/find/username/result";
    }

    @GetMapping("/find/username/result")
    public String findUsernameResult(Model model) {
        // 결과 페이지 렌더링
        return "user/find-username-result";
    }

    @GetMapping("/find/password")
    public String findPassword() {
        return "user/find-password";
    }

    @PostMapping("/find/password")
    public String findPasswordPost(@ModelAttribute UserDto userDto, BindingResult result,
                                   RedirectAttributes ra, HttpSession session) {

        if(result.hasErrors()) {
            return "user/find-password";
        }

        UserDto findUsername = userService.findByUsername(userDto.getUserName());
        if(findUsername != null) {
            //사용자 인증
            userSecurityService.authenticateUserAfterReset(findUsername.getUserName(), session);
            ra.addFlashAttribute("username", findUsername.getUserName());
            return "redirect:/user/change-password";
        } else {
            ra.addFlashAttribute("error", "일치하는 회원 정보가 없습니다.");
            return "redirect:/user/find/password";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(HttpSession session, Principal principal, Model model) {
        if(principal != null) {
            UserDto user = userService.findByUsername(principal.getName());
            model.addAttribute("username", user.getUserName());
        } else if (session.getAttribute("username") != null) {
            String findUsername = (String) session.getAttribute("username");
            model.addAttribute("username", findUsername);
        } else {
            return "redirect:/login";
        }
        return "user/change-password";
    }

    @GetMapping("/info")
    public String info(Model model, Principal principal) {
        UserDto user = userService.findByUsername(principal.getName());
        List<Long> addressId = userService.getMemberAddress(user.getUserId());

        log.info("RESULT: {}", addressId);
//        log.info("addressId : {}", addressId);
        model.addAttribute("loggedUser", user);
        model.addAttribute("addressId", addressId);
        return "user/info";
    }
}
