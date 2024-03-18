package com.moz1mozi.mybatis.member.service;

import com.moz1mozi.mybatis.member.dto.Role;
import com.moz1mozi.mybatis.member.dao.MemberDao;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Long insertMember(MemberDto member) {
        Role role = mapStringToRole(member.getRole().getDisplayName());
        log.info("role ={}", role);
        MemberDto siteUser = MemberDto.builder()
                .username(member.getUsername())
                .password(passwordEncoder.encode(member.getPassword()))
                .nickname(member.getNickname())
                .email(member.getEmail())
                .zipcode(member.getZipcode())
                .streetAddress(member.getStreetAddress())
                .detailAddress(member.getDetailAddress())
                .created_at(Date.from(Instant.now()))
                .role(role)
                .build();

        memberDao.insertMember(siteUser);
        return siteUser.getMember_id();
    }

    private Role mapStringToRole(String roleString) {
        for(Role role : Role.values()) {
            if(role.getDisplayName().equals(roleString)) {
                return role;
            }
        }
        return Role.BUYER;
    }

    public long deleteMember(Long memberId) {
        return memberDao.deleteMember(memberId);
    }


    public MemberDto findByUsername(String username) {
        String name = Optional.ofNullable(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
        return memberDao.findByUsername(name);
    }
}
