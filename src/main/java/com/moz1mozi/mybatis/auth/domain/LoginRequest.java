package com.moz1mozi.mybatis.auth.domain;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String userPw;
}
