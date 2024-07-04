package com.moz1mozi.mybatis.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private Long userId;
    private String userName;
    private String userPw;
    private String userEmail;
    private String userNickname;
    private Date userCreatedAt;
    private Date userModifiedAt;
    private Role userRole;
}
