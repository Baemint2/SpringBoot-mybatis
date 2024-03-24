package com.moz1mozi.mybatis.dao.member;

import com.moz1mozi.mybatis.exception.CustomException;
import com.moz1mozi.mybatis.member.dto.FindMemberDto;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.dao.MemberDao;
import com.moz1mozi.mybatis.member.dto.PasswordChangeDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.member.dto.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class MemberServiceTest {


    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원등록테스트() {
        MemberDto member = MemberDto.builder()
                .username("mary")
                .nickname("마리")
                .password("1234")
                .email("test@test.com")
                .created_at(Date.from(Instant.now()))
                .zipcode("서울시")
                .streetAddress("마포구")
                .detailAddress("서강동")
                .role(Role.BUYER)
                .build();
       memberService.insertMember(member);
        MemberDto savedMember = memberDao.findByUsername(member.getUsername());
        assertNotNull(savedMember);
        assertEquals(member.getUsername(), savedMember.getUsername());
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

    @Test
    void 비밀번호변경_테스트() {
        String currentPassword = "1234";
        MemberDto memberDto = MemberDto.builder()
                .username("admin")
                .nickname("이게모지")
                .password(passwordEncoder.encode(currentPassword))
                .email("admin@admin.com")
                .role(Role.ADMIN)
                .build();
        memberDao.insertMember(memberDto);

        //When
        String newPassword = "qwer";
        PasswordChangeDto passwordChangeDto =  PasswordChangeDto.builder()
                .username(memberDto.getUsername())
                .currentPassword(currentPassword)
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();

        boolean result = memberService.changePassword(passwordChangeDto);

        assertThat(result).isTrue();
        MemberDto updatedUser = memberDao.findByUsername(memberDto.getUsername());
        assertThat(passwordEncoder.matches(newPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    void 아이디찾기_성공_테스트() {
        String email = "emozi@emozi.com";
        FindMemberDto findMemberDto = FindMemberDto.builder()
                .email(email)
                .build();

        String username = memberService.findByUsernameEmail(findMemberDto);
        assertNotNull(username);
    }

    @Test
    void 아이디찾기_실패_테스트() {
        String email = "notExists@email.com";
        FindMemberDto findMemberDto = FindMemberDto.builder()
                .email(email)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.findByUsernameEmail(findMemberDto);
        });
    }

    @Test
    void 닉네임변경_성공_테스트() {
        String username = "emozi";
        String nickname = "이모지";
        memberService.updateNickname(username, nickname);

        MemberDto member = memberService.findByUsername(username);
        assertEquals(nickname, member.getNickname());
    }

    @Test
    void 닉네임변경_중복검사() {
        // 테스트를 위한 초기 닉네임 설정
        String username = "emozi";
        String nickname = "이모지";
        memberService.updateNickname(username, nickname); // 중복되지 않는 닉네임으로 초기 설정

        // 중복 닉네임으로 변경 시도
        String duplicateNickname = "이모지"; // 이전 단계에서 이미 사용된 닉네임
        assertThrows(CustomException.class, () -> {
            memberService.updateNickname(username, duplicateNickname);
        });
    }
}