package com.moz1mozi.mybatis.user.dto;

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

        @Override
        public String toString() {
                return "UserDto{" +
                        "userId=" + userId +
                        ", userName='" + userName + '\'' +
                        ", userPw='" + userPw + '\'' +
                        ", confirmPassword='" + confirmPassword + '\'' +
                        ", userEmail='" + userEmail + '\'' +
                        ", userNickname='" + userNickname + '\'' +
                        ", userProfileImagePath='" + userProfileImagePath + '\'' +
                        ", userMobile='" + userMobile + '\'' +
                        ", userCreatedAt=" + userCreatedAt +
                        ", userModifiedAt=" + userModifiedAt +
                        ", userRole=" + userRole +
                        ", addressId=" + addressId +
                        '}';
        }

        private Long addressId;
}
