package com.moz1mozi.mybatis.auth.controller;

import com.moz1mozi.mybatis.auth.domain.LoginRequest;
import com.moz1mozi.mybatis.auth.service.AuthenticationService;
import com.moz1mozi.mybatis.common.utils.CookieUtil;
import com.moz1mozi.mybatis.common.utils.JwtUtil;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.service.CustomUserDetails;
import com.moz1mozi.mybatis.user.service.UserSecurityService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.moz1mozi.mybatis.common.utils.CookieUtil.deleteCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserSecurityService securityService;
    private final JwtUtil jwtUtil;

    // 로그인
    @PostMapping("/login/v1")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        log.info("로그인 요청 수신: {}", loginRequest.getUserName());
            UserDetails userDetails = authenticationService.login(loginRequest.getUserName(), loginRequest.getUserPw());
        log.info("로그인 성공: {}", userDetails.getUsername());

            if(userDetails instanceof CustomUserDetails) {
                UserDto userDto = ((CustomUserDetails) userDetails).getUserDto();
                setTokensAndCookies(userDto.getAccessToken(), userDto.getRefreshToken(), response);
                return ResponseEntity.ok(userDto);
            }
            return ResponseEntity.status(401).body("로그인 실패");
    }

    // 로그아웃
    @PostMapping("/logout/v1")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        authenticationService.logout(auth.getName());
        deleteCookie(response, "accessToken");
        deleteCookie(response, "refreshToken");

        log.info("로그아웃 성공: 쿠키 삭제");
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("refreshToken") String refreshToken) {

        log.info("refresh Token: {}", refreshToken);

        if(!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("검증된 토큰이 아닙니다.");
        }

        String username = jwtUtil.getUsernameFromToken(refreshToken);
        UserDetails userDetails = securityService.loadUserByUsername(username);

        if(userDetails instanceof CustomUserDetails customUserDetails) {
            String newAccessToken = jwtUtil.createAccessToken(customUserDetails);
            log.info("refreshToken: {}", refreshToken);
            log.info("newAccessToken: {}", newAccessToken);

            customUserDetails.getUserDto().setAccessToken(newAccessToken);

            return ResponseEntity.ok(customUserDetails.getUserDto());
        }

        return ResponseEntity.status(401).body("Invalid refresh token");
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
