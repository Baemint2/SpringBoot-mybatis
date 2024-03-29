package com.moz1mozi.mybatis.member.service;

import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dto.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberMapper memberMapper;

    public List<MemberInfoDto> getMemberInfo() {
        return memberMapper.selectMemberInfo();
    }

    @PreAuthorize("hasRole('관리자')")
    @Transactional
    public void removeMember(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String adminName = null;
        if(authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if(principal instanceof UserDetails) {
                adminName = ((UserDetails)principal).getUsername();
            } else if (principal instanceof String) {
                adminName = principal.toString();
            }
        }

        memberMapper.deleteMember(username);
        log.info("관리자: {} 삭제한 유저: {}", adminName, username);
    }
}
