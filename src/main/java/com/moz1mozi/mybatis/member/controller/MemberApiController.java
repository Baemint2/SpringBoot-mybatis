package com.moz1mozi.mybatis.member.controller;

import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.dto.MemberWithdrawalDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
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


    //회원 탈퇴
    @DeleteMapping("/withdrawal/{username}")
    public ResponseEntity<?> withdrawal(@PathVariable String username,
                                        @RequestBody MemberWithdrawalDto withdrawalDto,
                                        HttpServletRequest request) {
        boolean withdrawalMember = memberService.deleteMember(withdrawalDto.getUsername(), withdrawalDto.getPassword());
        if(withdrawalMember) {
            SecurityContextHolder.clearContext();

            HttpSession session = request.getSession(false);
            if(session != null) {
                session.invalidate();
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("사용자 삭제 실패: 사용자명 또는 비밀번호가 일치하지 않습니다.");
        }

    }

    @GetMapping("/info/")
    public ResponseEntity<MemberDto> getMemberInfo(Principal principal) {
        MemberDto member = memberService.findByUsername(principal.getName());
        if(member != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // 닉네임 변경
    @PostMapping("/updateNickname")
    public ResponseEntity<?> updateNickname(@RequestParam String nickname, Principal principal) {
        String username = principal.getName();
        memberService.updateNickname(username, nickname);
        Map<String, String> response = Collections.singletonMap("nickname", nickname);
        return ResponseEntity.ok(response);
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
