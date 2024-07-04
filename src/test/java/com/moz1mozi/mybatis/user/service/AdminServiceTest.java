package com.moz1mozi.mybatis.user.service;

import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AdminServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminService adminService;

    @Test
    void 전체회원검색() {
        List<UserInfoDto> memberInfo = adminService.getMemberInfo();
        assertEquals(33, memberInfo.size());
    }

}