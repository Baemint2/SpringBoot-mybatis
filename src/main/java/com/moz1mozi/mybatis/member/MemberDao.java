package com.moz1mozi.mybatis.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    int insertMember(MemberDto member);

    // 회원 정보 조회
    MemberDto selectMember(Long memberId);

    void updateMember(MemberDto memberDto);

    Long deleteMember(Long memberId);


    MemberDto findByUsername(String username);
}
