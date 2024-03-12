package com.moz1mozi.mybatis.dao.member;

import com.moz1mozi.mybatis.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao {
    int insertMember(MemberDto member);

    // 회원 정보 조회
    MemberDto selectMember(Long memberId);

    void updateMember(MemberDto memberDto);

    Long deleteMember(Long memberId);
}
