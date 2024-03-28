package com.moz1mozi.mybatis.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
        private Long memberId;
        private String username;
        private String password;
        private String confirmPassword;
        private String email;
        private String nickname;
        private String zipcode;
        private String streetAddress;
        private String detailAddress;
        private Date created_at;
        private Date modified_at;
        private Role role;
 }
