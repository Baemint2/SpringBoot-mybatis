package com.moz1mozi.mybatis.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FindUserDto {
    private String userName;
    private String userNickname;
    private String userEmail;
    private Date userCreateDt;
}
