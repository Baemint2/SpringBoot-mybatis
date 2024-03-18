package com.moz1mozi.mybatis.member.dao;

import com.moz1mozi.mybatis.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    Long insertMember(MemberDto member);

    // 회원 정보 조회
    MemberDto selectMember(Long memberId);

    void updateMember(MemberDto memberDto);

    Long deleteMember(Long memberId);


    MemberDto findByUsername(String username);

    Long findByMemberIdByUsername(String username);

    MemberDto findByRoleWithUsername(String username);
}
