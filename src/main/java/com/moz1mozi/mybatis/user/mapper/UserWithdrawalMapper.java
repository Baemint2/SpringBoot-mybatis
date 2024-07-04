package com.moz1mozi.mybatis.user.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserWithdrawalMapper {
    int insertMemberWithdrawal(String username);
}
