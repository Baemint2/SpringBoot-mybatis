package com.moz1mozi.mybatis.member.controller;

import com.moz1mozi.mybatis.config.IsAdmin;
import com.moz1mozi.mybatis.member.dto.MemberInfoDto;
import com.moz1mozi.mybatis.member.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @IsAdmin
    @GetMapping("/admin/memberInfo")
    public ResponseEntity<List<MemberInfoDto>> getMemberInfo() {
        List<MemberInfoDto> memberInfo = adminService.getMemberInfo();
        return ResponseEntity.ok(memberInfo);
    }

    @Transactional
    @IsAdmin
    @DeleteMapping("/api/v1/admin/member/{username}")
    public ResponseEntity<?> removeMember(@PathVariable String username) {
        adminService.removeMember(username);
        return ResponseEntity.ok().body("회원 탈퇴가 정상적으로 되었습니다.");
    }
}
