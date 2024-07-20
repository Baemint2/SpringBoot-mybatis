package com.moz1mozi.mybatis.auth.service;


import com.moz1mozi.mybatis.common.utils.JwtUtil;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.CustomUserDetails;
import com.moz1mozi.mybatis.user.service.UserSecurityService;
import com.moz1mozi.mybatis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserSecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;


    // 로그인 시 토큰 부여
    public UserDetails login(String username, String password) {
        log.info("login 호출 테스트");
        UserDetails userDetails = securityService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new IllegalArgumentException("비밀번호를 확인해주세요.");
        }

        if(userDetails instanceof CustomUserDetails customUserDetails) {
            UserDto userDto = customUserDetails.getUserDto();
            String accessToken = jwtUtil.createAccessToken(customUserDetails);
            String refreshToken = jwtUtil.createRefreshToken(customUserDetails);
            log.info("AccessToken 생성: {}", accessToken);
            log.info("RefreshToken 생성: {}", refreshToken);
            userDto.setAccessToken(accessToken);
            userService.updateRefreshToken(refreshToken, userDto.getUserId());
            userDto.setRefreshToken(refreshToken);
            return customUserDetails;
        }

        return userDetails;
    }

}
