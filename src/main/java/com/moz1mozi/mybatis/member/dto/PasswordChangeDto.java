package com.moz1mozi.mybatis.member.dto;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "새 비밀번호는 공백일 수 없습니다.")
    private String newPassword;
    private String confirmPassword;

}
