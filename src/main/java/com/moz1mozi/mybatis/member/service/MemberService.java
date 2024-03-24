package com.moz1mozi.mybatis.member.service;

import com.moz1mozi.mybatis.exception.CustomException;
import com.moz1mozi.mybatis.member.dto.FindMemberDto;
import com.moz1mozi.mybatis.member.dto.PasswordChangeDto;
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

       if(!member.getPassword().equals(member.getConfirmPassword())) {
           throw new CustomException("confirmPassword", "비밀번호가 일치하지 않습니다");
       }

       if(memberDao.existsByEmail(member.getEmail())) {
           throw new CustomException("email", "이미 등록된 이메일입니다.");
       }

       if(memberDao.existsByUsername(member.getUsername())) {
           throw new CustomException("username", "이미 등록된 사용자명입니다.");
       }

       if(memberDao.existsByNickname(member.getNickname())) {
           throw new CustomException("nickname", "이미 등록된 닉네임입니다.");
       }

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

    @Transactional
    public boolean deleteMember(String username, String password) {
        MemberDto member = Optional.ofNullable(memberDao.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
       log.info("유저 찾기 = {}", member.getUsername());
       if(passwordEncoder.matches(password, member.getPassword())) {
           memberDao.deleteMember(member.getUsername());
           return true;
       }
       return false;
    }

    @Transactional
    public MemberDto findByUsername(String username) {
        String name = Optional.ofNullable(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
        return memberDao.findByUsername(name);
    }

    // 비밀번호 변경
    @Transactional
    public boolean changePassword(PasswordChangeDto passwordChangeDto) {
        MemberDto member = memberDao.findByUsername(passwordChangeDto.getUsername());
        if(member == null || !passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), member.getPassword())) {
            return false;
        }
        if(!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            return false;
        }

        String encodedPassword = passwordEncoder.encode(passwordChangeDto.getNewPassword());
        memberDao.updatePassword(passwordChangeDto.getUsername(), encodedPassword);
        return true;
    }

    //아이디 찾기
    @Transactional(readOnly = true)
    public String findByUsernameEmail(FindMemberDto findMemberDto) {
        return Optional.ofNullable(memberDao.findUsernameByEmail(findMemberDto.getEmail()))
                                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

    }

    //닉네임 변경
    @Transactional
    public void updateNickname(String username, String nickname) {
        if(memberDao.findByUsername(username) == null) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
        //중복 검사
        if(nicknameDuplicate(nickname)) {
            throw new CustomException("isNicknameDuplicate","이미 사용중인 닉네임입니다.");
        }

        memberDao.updateNickname(username, nickname);
    }

    //사용자명 중복 검사
    public boolean usernameDuplicate(String username) {
        return memberDao.existsByUsername(username);
    }

    //이메일 중복 검사
    public boolean emailDuplicate(String email) {
        return memberDao.existsByEmail(email);
    }

    //닉네임 중복 검사
    public boolean nicknameDuplicate(String nickname) {
        return memberDao.existsByNickname(nickname);
    }

}
