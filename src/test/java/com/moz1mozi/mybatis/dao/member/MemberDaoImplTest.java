package com.moz1mozi.mybatis.dao.member;

import com.moz1mozi.mybatis.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberDaoImplTest {

    @Autowired
    private MemberDaoImpl memberDao;

    @Test
    void 회원등록테스트() {
        MemberDto member = MemberDto.builder()
                .username("tester")
                .password("1234")
                .email("test@test.com")
                .build();
        int result = memberDao.insertBoard(member);
        assertEquals(1, result);
    }

    @Test
    void 회원삭제테스트() {
        long deleteCount = memberDao.deleteBoard(1L);
        assertEquals(1, deleteCount);
    }
}