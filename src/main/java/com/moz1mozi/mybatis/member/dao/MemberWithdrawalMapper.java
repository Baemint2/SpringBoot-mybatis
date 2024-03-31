package com.moz1mozi.mybatis.member.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberWithdrawalMapper {
    int insertMemberWithdrawal(String username);
}
