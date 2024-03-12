package com.moz1mozi.mybatis.dao.member;

import com.moz1mozi.mybatis.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDaoImpl {

    private final MemberDao memberDao;

    public int insertBoard(MemberDto member) {
        return memberDao.insertMember(member);
    }

    public long deleteBoard(Long memberId) {
        return memberDao.deleteMember(memberId);
    }

}
