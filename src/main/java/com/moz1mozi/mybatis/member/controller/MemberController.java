package com.moz1mozi.mybatis.member.controller;

import com.moz1mozi.mybatis.delivery.dto.ShippingAddressDto;
import com.moz1mozi.mybatis.delivery.service.ShippingAddressService;
import com.moz1mozi.mybatis.member.dto.FindMemberDto;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberSecurityService;
import com.moz1mozi.mybatis.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberSecurityService memberSecurityService;
    private final ShippingAddressService shippingAddressService;

    @GetMapping("/signup")
    public String signup() {
        return "member/signup-form";
    }

    @GetMapping("/login")
    public String login() {
        return "member/login-form";
    }

    @GetMapping("/find/username")
    public String findUsername() {
        return "member/find-username";
    }

    @PostMapping("/find/username")
    public String findUsernamePost(@ModelAttribute MemberDto memberDto, BindingResult result,
                                   RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "member/find-username";
        }

        MemberDto findUsername = memberService.getEmail(memberDto.getEmail());
        if (findUsername != null) {
            ra.addFlashAttribute("findUsername", findUsername.getUsername());
        } else {
            ra.addFlashAttribute("error", "해당 이메일로 가입된 아이디가 없습니다.");
        }
        return "redirect:/member/find/username/result";
    }

    @GetMapping("/find/username/result")
    public String findUsernameResult(Model model) {
        // 결과 페이지 렌더링
        return "member/find-username-result";
    }

    @GetMapping("/find/password")
    public String findPassword() {
        return "member/find-password";
    }

    @PostMapping("/find/password")
    public String findPasswordPost(@ModelAttribute MemberDto memberDto, BindingResult result,
                                   RedirectAttributes ra, HttpSession session) {

        if(result.hasErrors()) {
            return "member/find-password";
        }

        MemberDto findUsername = memberService.findByUsername(memberDto.getUsername());
        if(findUsername != null) {
            //사용자 인증
            memberSecurityService.authenticateUserAfterReset(findUsername.getUsername(), session);
            ra.addFlashAttribute("username", findUsername.getUsername());
            return "redirect:/member/change-password";
        } else {
            ra.addFlashAttribute("error", "일치하는 회원 정보가 없습니다.");
            return "redirect:/member/find/password";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(HttpSession session, Principal principal, Model model) {
        if(principal != null) {
            MemberDto member = memberService.findByUsername(principal.getName());
            model.addAttribute("username", member.getUsername());
        } else if (session.getAttribute("username") != null) {
            String findUsername = (String) session.getAttribute("username");
            model.addAttribute("username", findUsername);
        } else {
            return "redirect:/login";
        }
        return "member/change-password";
    }

    @GetMapping("/info")
    public String info(Model model, Principal principal) {
        MemberDto member = memberService.findByUsername(principal.getName());
        List<Long> addressId = memberService.getMemberAddress(member.getMemberId());

        log.info("RESULT: {}", addressId);
//        log.info("addressId : {}", addressId);
        model.addAttribute("loggedUser", member);
        model.addAttribute("addressId", addressId);
        return "member/info";
    }
}
