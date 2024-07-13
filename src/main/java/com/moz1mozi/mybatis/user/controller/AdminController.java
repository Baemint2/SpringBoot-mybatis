package com.moz1mozi.mybatis.user.controller;

import com.moz1mozi.mybatis.common.config.IsAdmin;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.dto.UserInfoDto;
import com.moz1mozi.mybatis.user.service.AdminService;
import com.moz1mozi.mybatis.user.service.UserService;
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
    private final UserService userService;

    @IsAdmin
    @GetMapping("/admin/userInfo")
    public ResponseEntity<List<UserInfoDto>> getMemberInfo() {
        List<UserInfoDto> userInfo = adminService.getMemberInfo();
        return ResponseEntity.ok(userInfo);
    }

    @IsAdmin
    @GetMapping("/admin/register-user")
    public String showRegister() {
        return "admin/register-user";
    }

    @IsAdmin
    @PostMapping("/api/v1/admin/register-user")
    public ResponseEntity<Map<String, String>> registerMember(@RequestPart("user") UserDto userDto,
                                                             @RequestPart(value = "file", required = false)MultipartFile file) throws IOException {
        adminService.insertMember(userDto, file);
        return ResponseEntity.ok(Map.of("message", "회원등록이 완료되었습니다."));
    }

    @IsAdmin
    @DeleteMapping("/api/v1/admin/user/{userName}")
    public ResponseEntity<?> removeMember(@PathVariable String userName) {
        adminService.removeMember(userName);
        return ResponseEntity.ok().body("회원 탈퇴가 정상적으로 되었습니다.");
    }
}
