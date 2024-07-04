package com.moz1mozi.mybatis.member.dto;

import lombok.*;

import java.util.Date;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
        private Long userId;
        private String userName;
        private String userPw;
        private String confirmPassword;
        private String userEmail;
        private String userNickname;
        private String userProfileImagePath;
        private String userMobile;
        private Date userCreatedAt;
        private Date userModifiedAt;
        private Role userRole;
        private Long addressId;
}
