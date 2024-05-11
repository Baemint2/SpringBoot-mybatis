package com.moz1mozi.mybatis.member.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
        private Long memberId;
        private String username;
        private String password;
        private String confirmPassword;
        private String email;
        private String nickname;
        private String profileImagePath;
        private String mobile;
        private Date createdAt;
        private Date modifiedAt;
        private Role role;
        private Long addressId;
}
