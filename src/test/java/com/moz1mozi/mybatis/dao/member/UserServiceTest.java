package com.moz1mozi.mybatis.dao.member;

import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.user.dto.FindUserDto;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.PasswordChangeDto;
import com.moz1mozi.mybatis.user.service.UserService;
import com.moz1mozi.mybatis.user.dto.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@SpringBootTest
class UserServiceTest {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원등록테스트() throws IOException {
        UserDto user = UserDto.builder()
                .userName("tester100")
                .userNickname("tester100")
                .userPw("1234")
                .confirmPassword("1234")
                .userEmail("test100@test.com")
                .userCreatedAt(Date.from(Instant.now()))
                .userRole(Role.BUYER)
                .build();
        log.info(user.getUserPw());
       userService.insertMember(user, null);
        UserDto savedMember = userMapper.findByUsername(user.getUserName()).orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
        assertNotNull(savedMember);
        assertEquals(user.getUserName(), savedMember.getUserName());
    }

    @Test
    @Transactional
    void 유저찾기() {
        UserDto user = UserDto.builder()
                .userName("moz1mozi")
                .userPw("1234")
                .userEmail("moz1mozi@mozi.com")
                .userCreatedAt(Date.from(Instant.now()))
                .userRole(Role.SELLER)
                .build();

        UserDto byUsername = userService.findByUsername(user.getUserName());
        assertEquals(user.getUserName(), byUsername.getUserName());
    }

    @Test
    void 비밀번호변경_테스트() {
        String username = "tester100";
        String currentPassword = "1234";
        String encodedPassword = passwordEncoder.encode(currentPassword);
        UserDto existingMember = userService.findByUsername(username);
        assertThat(passwordEncoder.matches(currentPassword, existingMember.getUserPw())).isTrue();
        System.out.println(encodedPassword);

        //When
        String newPassword = "qwer";
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        PasswordChangeDto passwordChangeDto =  PasswordChangeDto.builder()
                .userName(username)
                .currentPassword(currentPassword)
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();
        log.info("현재 유저의 비밀번호: {}, 현재 비밀번호 : {}, 이것도 현재 비밀번호 : {}",existingMember.getUserPw() , passwordChangeDto.getCurrentPassword(), passwordChangeDto.getConfirmPassword());
        userService.changePassword(username, passwordChangeDto);

        UserDto updatedUser = userMapper.findByUsername(username).orElseThrow();
        assertThat(passwordEncoder.matches(newPassword, updatedUser.getUserPw())).isTrue();
    }

    @Test
    void 아이디찾기_성공_테스트() {
        String email = "emozi@emozi.com";
        FindUserDto findUserDto = FindUserDto.builder()
                .userEmail(email)
                .build();

        String username = userService.findByUsernameEmail(findUserDto);
        assertNotNull(username);
    }

    @Test
    void 아이디찾기_실패_테스트() {
        String email = "notExists@email.com";
        FindUserDto findUserDto = FindUserDto.builder()
                .userEmail(email)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            userService.findByUsernameEmail(findUserDto);
        });
    }

    @Test
    void 닉네임변경_성공_테스트() {
        String username = "emozi";
        String nickname = "이모히지";
        userService.updateNickname(username, nickname);

        UserDto user = userService.findByUsername(username);
        assertEquals(nickname, user.getUserNickname());
    }

    @Test
    void 닉네임변경_중복검사() {
        // 테스트를 위한 초기 닉네임 설정
        String username = "emozi";
        String nickname = "이모지히";
        userService.updateNickname(username, nickname); // 중복되지 않는 닉네임으로 초기 설정

        // 중복 닉네임으로 변경 시도
        String duplicateNickname = "이모지히"; // 이전 단계에서 이미 사용된 닉네임
        assertThrows(CustomException.class, () -> {
            userService.updateNickname(username, duplicateNickname);
        });
    }
}