package com.moz1mozi.mybatis.user.controller;

import com.moz1mozi.mybatis.common.config.IsAdmin;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.dto.UserInfoDto;
import com.moz1mozi.mybatis.user.service.AdminService;
import com.moz1mozi.mybatis.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final MemberService memberService;

    @IsAdmin
    @GetMapping("/admin/memberInfo")
    public ResponseEntity<List<UserInfoDto>> getMemberInfo() {
        List<UserInfoDto> memberInfo = adminService.getMemberInfo();
        return ResponseEntity.ok(memberInfo);
    }

    @IsAdmin
    @GetMapping("/admin/register-member")
    public String showRegister() {
        return "admin/register-member";
    }

    @IsAdmin
    @PostMapping("/api/v1/admin/register-member")
    public ResponseEntity<Map<String, String>> registerMember(@RequestPart("member") UserDto memberDto,
                                                             @RequestPart(value = "file", required = false)MultipartFile file) throws IOException {
        adminService.insertMember(memberDto, file);
        return ResponseEntity.ok(Map.of("message", "회원등록이 완료되었습니다."));
    }

    @IsAdmin
    @DeleteMapping("/api/v1/admin/member/{username}")
    public ResponseEntity<?> removeMember(@PathVariable String username) {
        adminService.removeMember(username);
        return ResponseEntity.ok().body("회원 탈퇴가 정상적으로 되었습니다.");
    }
}
