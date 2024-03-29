package com.moz1mozi.mybatis.member.dao;

import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.dto.MemberInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    Long insertMember(MemberDto member);

    // 회원 정보 조회
    List<MemberInfoDto> selectMemberInfo();

    Long deleteMember(String username);

    MemberDto findByUsername(String username);

    Long findByMemberIdByUsername(String username);


    // 이메일로 아이디(username) 찾기
    String findUsernameByEmail(String email);
    // 비밀번호 변경
    void updatePassword(@Param("username")String username, @Param("password")String password);

    // 닉네임 변경
    void updateNickname(@Param("username")String username, @Param("nickname")String nickname);
    // 중복 검사
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}
