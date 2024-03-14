package com.moz1mozi.mybatis.member;

import com.moz1mozi.mybatis.member.Role;
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
        private Long member_id;
        private String username;
        private String password;
        private String confirmPassword;
        private String email;
        private String nickname;
        private String address1;
        private String address2;
        private String address3;
        private Date created_at;
        private Date modified_at;
        private Role role;
 }
