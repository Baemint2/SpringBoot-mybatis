package com.moz1mozi.mybatis.member.controller;

import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/member")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody MemberDto memberDto) {
        Long memberId = memberService.insertMember(memberDto);
        log.info("회원가입 = {}", memberId);
        return ResponseEntity.ok(Map.of("message", "회원가입이 성공적으료 완료되었습니다."));
    }

    @GetMapping("/username/check")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username")String username) {
        boolean isUsernameDuplicate = memberService.usernameDuplicate(username);
        log.info("중복된 아이디 = {}", isUsernameDuplicate);
        return ResponseEntity.ok(Map.of("isUsernameDuplicate",isUsernameDuplicate));
    }

    @GetMapping("/email/check")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email")String email) {
        boolean isEmailDuplicate = memberService.emailDuplicate(email);
        log.info("중복된 이메일 = {}", isEmailDuplicate);
        return ResponseEntity.ok(Map.of("isEmailDuplicate",isEmailDuplicate));
    }

    @GetMapping("/nickname/check")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam("nickname")String nickname) {
        boolean isNicknameDuplicate = memberService.nicknameDuplicate(nickname);
        log.info("중복된 닉네임 = {}", isNicknameDuplicate);
        return ResponseEntity.ok(Map.of("isNicknameDuplicate", isNicknameDuplicate));
    }

}
