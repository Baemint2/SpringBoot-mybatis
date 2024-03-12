package com.moz1mozi.mybatis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
        private Long member_id;
        private String username;
        private String password;
        private String confirmPassword;
        private String email;
        private String nickname;
        private String address1;
        private String address2;
        private String address3;
 }
