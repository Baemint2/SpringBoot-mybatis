package com.moz1mozi.mybatis.utils;

import java.security.SecureRandom;

public class VerificationCodeUtils {
    public static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    public static final String NUMBER = "0123456789";
    public static final String CODE_ALLOW = CHAR_LOWER + CHAR_UPPER + NUMBER;
    public static final int CODE_LENGTH = 6;

    public static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CODE_ALLOW.charAt(random.nextInt(CODE_ALLOW.length())));
        }
        return sb.toString();
    }
}
