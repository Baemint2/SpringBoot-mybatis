package com.moz1mozi.mybatis.auth;

import com.moz1mozi.mybatis.auth.service.AuthenticationService;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthenticationServiceTest {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void login() {
        String username = "emozi";
        String password = "1234";
        passwordEncoder.encode(password);

        UserDetails login = authenticationService.login(username, password);
        String username1 = login.toString();
        log.info("username1: {}", username1);
    }
}