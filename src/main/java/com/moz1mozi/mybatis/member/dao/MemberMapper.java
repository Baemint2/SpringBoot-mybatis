package com.moz1mozi.mybatis.member.dao;

import com.moz1mozi.mybatis.member.dto.FindMemberDto;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.dto.MemberInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    Long insertMember(MemberDto member);

    // 회원 정보 조회
    List<MemberInfoDto> selectMemberInfo();

    Long deleteMember(String username);

    MemberDto findByUsername(String username);

    Long findByMemberId(Long memberId);

    Long findByMemberIdByUsername(String username);

    List<Long> findMemberIdByAddressId(Long memberId);

    // 닉네임와 이메일로 해당하는 회원의 정보 찾기
    Optional<FindMemberDto> findByNicknameAndEmail(@Param("nickname")String nickname, @Param("email") String email);

    // 닉네임, 이메일, 아이디에 해당하는 회원의 정보 찾기
    Optional<FindMemberDto> findByNicknameAndEmailAndUsername(@Param("nickname") String nickname, @Param("email") String email, @Param("username") String username);

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
