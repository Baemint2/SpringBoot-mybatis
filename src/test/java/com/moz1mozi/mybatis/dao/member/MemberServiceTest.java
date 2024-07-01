package com.moz1mozi.mybatis.dao.member;

import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.member.dto.FindMemberDto;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dto.PasswordChangeDto;
import com.moz1mozi.mybatis.member.service.MemberService;
import com.moz1mozi.mybatis.member.dto.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@SpringBootTest
class MemberServiceTest {


    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Test
//    void 회원등록테스트() {
//        MemberDto member = MemberDto.builder()
//                .username("mary")
//                .nickname("마리")
//                .password("1234")
//                .email("test@test.com")
//                .createdAt(Date.from(Instant.now()))
//                .role(Role.BUYER)
//                .build();
//       memberService.insertMember(member);
//        MemberDto savedMember = memberMapper.findByUsername(member.getUsername());
//        assertNotNull(savedMember);
//        assertEquals(member.getUsername(), savedMember.getUsername());
//    }

    @Test
    void 유저찾기() {
        MemberDto member = MemberDto.builder()
                .username("moz1mozi")
                .password("1234")
                .email("moz1mozi@mozi.com")
                .createdAt(Date.from(Instant.now()))
                .role(Role.SELLER)
                .build();

        MemberDto byUsername = memberService.findByUsername(member.getUsername());
        assertEquals("moz1mozi", byUsername);
    }

    @Test
    void 비밀번호변경_테스트() {
        String username = "admin";
        String currentPassword = "1234";
        String encodedPassword = passwordEncoder.encode(currentPassword);
        MemberDto existingMember = memberService.findByUsername(username);
//        assertThat(passwordEncoder.matches(currentPassword, existingMember.getPassword())).isTrue();
        System.out.println(encodedPassword);

        //When
        String newPassword = "qwer";
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        PasswordChangeDto passwordChangeDto =  PasswordChangeDto.builder()
                .username(username)
                .currentPassword(encodedPassword)
                .newPassword(encodedNewPassword)
                .confirmPassword(encodedNewPassword)
                .build();

        memberService.changePassword(username, passwordChangeDto);

        MemberDto updatedUser = memberMapper.findByUsername(username).orElseThrow();
//        assertThat(passwordEncoder.matches(newPassword, updatedUser.getPassword())).isTrue();
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
        String nickname = "이모지히";
        memberService.updateNickname(username, nickname);

        MemberDto member = memberService.findByUsername(username);
        assertEquals(nickname, member.getNickname());
    }

    @Test
    void 닉네임변경_중복검사() {
        // 테스트를 위한 초기 닉네임 설정
        String username = "emozi";
        String nickname = "이모지히";
        memberService.updateNickname(username, nickname); // 중복되지 않는 닉네임으로 초기 설정

        // 중복 닉네임으로 변경 시도
        String duplicateNickname = "이모지히"; // 이전 단계에서 이미 사용된 닉네임
        assertThrows(CustomException.class, () -> {
            memberService.updateNickname(username, duplicateNickname);
        });
    }
}