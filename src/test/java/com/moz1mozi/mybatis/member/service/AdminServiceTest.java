package com.moz1mozi.mybatis.member.service;

import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dto.MemberInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AdminServiceTest {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private AdminService adminService;

    @Test
    void 전체회원검색() {
        List<MemberInfoDto> memberInfo = adminService.getMemberInfo();
        assertEquals(33, memberInfo.size());
    }

}