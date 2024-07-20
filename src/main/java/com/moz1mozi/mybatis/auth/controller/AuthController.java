package com.moz1mozi.mybatis.auth.controller;

import com.moz1mozi.mybatis.auth.domain.LoginRequest;
import com.moz1mozi.mybatis.auth.service.AuthenticationService;
import com.moz1mozi.mybatis.common.utils.JwtUtil;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.CustomUserDetails;
import com.moz1mozi.mybatis.user.service.UserSecurityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserSecurityService securityService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login/v1")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        log.info("로그인 요청 수신: {}", loginRequest.getUsername());
            UserDetails userDetails = authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
        log.info("로그인 성공: {}", userDetails.getUsername());

            if(userDetails instanceof CustomUserDetails) {
                UserDto userDto = ((CustomUserDetails) userDetails).getUserDto();
                setTokensAndCookies(userDto.getAccessToken(), userDto.getRefreshToken(), response);
                return ResponseEntity.ok(userDto);
            }
            return ResponseEntity.status(401).body("로그인 실패");
    }

    private void setTokensAndCookies(String accessToken, String refreshToken, HttpServletResponse response) {
        // JWT에서 직접 만료 시간을 추출하여 쿠키의 유효기간을 설정
        int accessTokenMaxAge = jwtUtil.getExpiryDurationFromToken(accessToken);
        int refreshTokenMaxAge = jwtUtil.getExpiryDurationFromToken(refreshToken);
        // 쿠키에 새 토큰 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setMaxAge(accessTokenMaxAge);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setMaxAge(refreshTokenMaxAge);
        response.addCookie(refreshTokenCookie);

        log.info("새로운 AccessToken: {}", accessToken);
        log.info("새로운 RefreshToken: {}", refreshToken);
    }

}
