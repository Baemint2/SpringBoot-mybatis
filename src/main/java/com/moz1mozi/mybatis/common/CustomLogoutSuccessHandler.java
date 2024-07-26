package com.moz1mozi.mybatis.common;

import com.moz1mozi.mybatis.auth.service.AuthenticationService;
import com.moz1mozi.mybatis.common.utils.CookieUtil;
import com.moz1mozi.mybatis.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {
            authenticationService.logout(authentication.getName());

            CookieUtil.deleteCookie(response, "accessToken");
            CookieUtil.deleteCookie(response, "refreshToken");

            response.sendRedirect("/user/login");
        }
    }
}
