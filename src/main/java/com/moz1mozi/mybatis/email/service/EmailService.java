package com.moz1mozi.mybatis.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    public static final String SENDER_EMAIL = "moz1mozi@example.com";
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendVerificationEmail(String to, String verificationCode) {
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);

        String processHtml = templateEngine.process("verificationEmail", context);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(SENDER_EMAIL);
            helper.setTo(to);
            helper.setSubject("회원가입 인증코드");
            helper.setText(processHtml, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            log.error("이메일 발송 실패: {}", ex.getMessage());
        }
    }
}
