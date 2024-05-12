package com.moz1mozi.mybatis.member.utils;

import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.dto.Role;

public class MemberUtils {

    public static void validateMemberData(MemberDto member, MemberMapper memberMapper, boolean checkPassword) {
        if(checkPassword && !member.getPassword().equals(member.getConfirmPassword())) {
            throw new CustomException("confirmPassword", "비밀번호가 일치하지 않습니다");
        }

        if(memberMapper.existsByEmail(member.getEmail())) {
            throw new CustomException("email", "이미 등록된 이메일입니다.");
        }

        if(memberMapper.existsByUsername(member.getUsername())) {
            throw new CustomException("username", "이미 등록된 사용자명입니다.");
        }

        if(memberMapper.existsByNickname(member.getNickname())) {
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
