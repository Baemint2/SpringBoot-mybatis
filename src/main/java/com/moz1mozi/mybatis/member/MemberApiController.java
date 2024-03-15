package com.moz1mozi.mybatis.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/member/signup")
    public ResponseEntity<?> signup(@RequestBody MemberDto memberDto) {
        Long memberId = memberService.insertMember(memberDto);
        log.info("회원가입 = {}", memberId);
        return ResponseEntity.ok(Map.of("message", "회원가입이 성공적으료 완료되었습니다."));
    }

}
