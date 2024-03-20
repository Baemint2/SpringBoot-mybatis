package com.moz1mozi.mybatis.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeDto {

    private String username;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

}
