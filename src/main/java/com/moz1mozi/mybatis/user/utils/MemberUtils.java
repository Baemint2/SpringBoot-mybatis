package com.moz1mozi.mybatis.user.utils;

import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.dto.Role;

public class MemberUtils {

    public static void validateMemberData(UserDto member, UserMapper userMapper, boolean checkPassword) {
        if(checkPassword && !member.getUserPw().equals(member.getConfirmPassword())) {
            throw new CustomException("confirmPassword", "비밀번호가 일치하지 않습니다");
        }

        if(userMapper.existsByEmail(member.getUserEmail())) {
            throw new CustomException("email", "이미 등록된 이메일입니다.");
        }

        if(userMapper.existsByUsername(member.getUserName())) {
            throw new CustomException("username", "이미 등록된 사용자명입니다.");
        }

        if(userMapper.existsByNickname(member.getUserNickname())) {
            throw new CustomException("nickname", "이미 등록된 닉네임입니다.");
        }
    }

    public static Role mapStringToRole(String roleString) {
        for(Role role : Role.values()) {
            if(role.getDisplayName().equals(roleString)) {
                return role;
            }
        }
        return Role.BUYER;
    }
}
