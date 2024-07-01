package com.moz1mozi.mybatis.email.service;

import com.moz1mozi.mybatis.email.dto.EmailVerificationDto;
import com.moz1mozi.mybatis.email.mapper.EmailVerificationMapper;
import com.moz1mozi.mybatis.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.moz1mozi.mybatis.common.utils.RandomCodeUtils.generateVerificationCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmailVerificationMapper emailVerificationMapper;
    private final EmailService emailService;
    private final MemberService memberService;

    //인증번호 전송 및 인증테이블에 저장
    public void sendAndSaveVerificationCode(String email) {
        Optional<EmailVerificationDto> existingEmail = Optional.ofNullable(emailVerificationMapper.findByEmail(email));

        existingEmail.ifPresent(emailVerificationMapper::delete);

        String verificationCode = generateVerificationCode();
        EmailVerificationDto emailVerificationDto = EmailVerificationDto.builder()
                .email(email)
                .verificationCode(verificationCode)
                .expiryDateTime(LocalDateTime.now().plusMinutes(5))
                .build();

        emailVerificationMapper.insertVerification(emailVerificationDto);

        emailService.sendVerificationEmail(email, verificationCode);
    }


    public boolean verifyCode(String email, String code) {
        Optional<EmailVerificationDto> emailVerificationDto = Optional.ofNullable(emailVerificationMapper.findByEmail(email));
        if(emailVerificationDto.isPresent()) {
            EmailVerificationDto emailVerification = emailVerificationDto.get();
            log.info("인증 정보: email={}, savedCode={}, expiryDateTime={}",
                    emailVerification.getEmail(), emailVerification.getVerificationCode(), emailVerification.getExpiryDateTime());

            LocalDateTime expiryDateTime = emailVerification.getExpiryDateTime();
            boolean isExpired = LocalDateTime.now().isAfter(expiryDateTime);
            if(isExpired) {
                log.info("인증 코드 만료 : {}", email);
                return false;
            }

            return emailVerification.getVerificationCode().equals(code);
        } else {
            log.info("인증 이메일 정보 없음: email={}", email);
            return false;
        }
    }

}
