package com.moz1mozi.mybatis.member.dao;

import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.dto.PasswordChangeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberDao {
    Long insertMember(MemberDto member);

    // 회원 정보 조회
    MemberDto selectMember(Long memberId);

    void updateMember(MemberDto memberDto);

    Long deleteMember(Long memberId);

    MemberDto findByUsername(String username);

    Long findByMemberIdByUsername(String username);

    // 유저 권한 찾기
    MemberDto findByRoleWithUsername(String username);

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
