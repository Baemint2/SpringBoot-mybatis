package com.moz1mozi.mybatis.email.controller;

import com.moz1mozi.mybatis.email.dto.EmailSendDto;
import com.moz1mozi.mybatis.email.dto.EmailVerificationDto;
import com.moz1mozi.mybatis.email.service.AuthenticationService;
import com.moz1mozi.mybatis.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailApiController {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/api/v1/email/send-verification-code")
    // 인증 코드 전송
    public ResponseEntity<?> sendVerificationCode(@Valid @RequestBody EmailSendDto emailSendDto) {
        String email = emailSendDto.getEmail();
        try {
            authenticationService.sendAndSaveVerificationCode(email);
            return ResponseEntity.ok().body(Map.of("message", "인증번호가 전송되었습니다."));
        } catch (Exception ex) {
            log.error("인증번호 전송 중 오류 발생: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "인증번호 전송 중 오류가 발생했습니다."));
        }
    }

    // 인증 코드 검증
    @PostMapping("/api/v1/email/verify-code")
    public ResponseEntity<?> verifyCode(@Valid @RequestBody EmailVerificationDto emailVerificationDto) {
        String email = emailVerificationDto.getEmail();
        String code = emailVerificationDto.getVerificationCode();

        log.info("인증 코드 검증 요청: email = {}, code = {}", email, code);

        try {
            boolean isCodeValid = authenticationService.verifyCode(email, code);
            if (isCodeValid) {
                log.info("인증 성공: email={}", email);
                return ResponseEntity.ok().body(Map.of("message", "인증 성공"));
            } else {
                log.info("인증 실패: email={}", email);
                return ResponseEntity.badRequest().body(Map.of("error", "인증 실패"));
            }
        } catch (Exception ex) {
            log.error("인증 코드 검증 중 오류 발생: email={}, error={}", email, ex.getMessage(), ex);
            return ResponseEntity.internalServerError().body(Map.of("error", "인증 코드 검증 중 오류가 발생했습니다."));
        }
    }
}
