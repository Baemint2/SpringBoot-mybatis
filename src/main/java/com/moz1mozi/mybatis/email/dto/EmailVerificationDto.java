package com.moz1mozi.mybatis.email.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationDto {

    private long verificationId;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@(?![a-zA-Z0-9.-]*xn--)[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    private LocalDateTime expiryDateTime;

    @NotEmpty(message = "유효한 인증번호를 입력해주세요")
    private String verificationCode;
}
