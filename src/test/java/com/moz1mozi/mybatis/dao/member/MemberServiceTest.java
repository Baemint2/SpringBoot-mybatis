package com.moz1mozi.mybatis.dao.member;

import com.moz1mozi.mybatis.member.MemberDto;
import com.moz1mozi.mybatis.member.MemberDao;
import com.moz1mozi.mybatis.member.MemberService;
import com.moz1mozi.mybatis.member.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class MemberServiceTest {


    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberService memberService;

    @Test
    void 회원등록테스트() {
        MemberDto member = MemberDto.builder()
                .username("mary")
                .nickname("마리")
                .password("1234")
                .email("test@test.com")
                .created_at(Date.from(Instant.now()))
                .address1("서울시")
                .address2("마포구")
                .address3("서강동")
                .role(Role.BUYER)
                .build();
       memberService.insertMember(member);
        MemberDto savedMember = memberDao.findByUsername(member.getUsername());
        assertNotNull(savedMember);
        assertEquals(member.getUsername(), savedMember.getUsername());
    }

    @Test
    void 회원삭제테스트() {
        long deleteCount = memberService.deleteMember(1L);
        assertEquals(1, deleteCount);
    }

    @Test
    void 유저찾기() {
        MemberDto member = MemberDto.builder()
                .username("moz1mozi")
                .password("1234")
                .email("moz1mozi@mozi.com")
                .created_at(Date.from(Instant.now()))
                .role(Role.SELLER)
                .build();

        MemberDto byUsername = memberService.findByUsername(member.getUsername());
        assertEquals("moz1mozi", byUsername);
    }
}