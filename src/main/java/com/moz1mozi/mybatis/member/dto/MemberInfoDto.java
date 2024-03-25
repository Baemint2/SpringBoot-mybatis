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
public class MemberInfoDto {
    private Long memberId;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String zipcode;
    private String streetAddress;
    private String detailAddress;
    private Date createdAt;
    private Date modifiedAt;
    private Role role;
}
