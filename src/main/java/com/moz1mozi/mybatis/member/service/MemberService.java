package com.moz1mozi.mybatis.member.service;

import com.moz1mozi.mybatis.exception.CustomException;
import com.moz1mozi.mybatis.exception.InvalidCurrentPasswordException;
import com.moz1mozi.mybatis.exception.PasswordsDoNotMatchException;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dao.MemberWithdrawalMapper;
import com.moz1mozi.mybatis.member.dto.*;
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

    private final MemberMapper memberMapper;
    private final MemberWithdrawalMapper memberWithdrawalMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long insertMember(MemberDto member) {
       if(!member.getPassword().equals(member.getConfirmPassword())) {
           throw new CustomException("confirmPassword", "비밀번호가 일치하지 않습니다");
       }

       if(memberMapper.existsByEmail(member.getEmail())) {
           throw new CustomException("email", "이미 등록된 이메일입니다.");
       }

       if(memberMapper.existsByUsername(member.getUsername())) {
           throw new CustomException("username", "이미 등록된 사용자명입니다.");
       }

       if(memberMapper.existsByNickname(member.getNickname())) {
           throw new CustomException("nickname", "이미 등록된 닉네임입니다.");
       }

        Role role = mapStringToRole(member.getRole().getDisplayName());
        log.info("role ={}", role);
        MemberDto siteUser = MemberDto.builder()
                .username(member.getUsername())
                .password(passwordEncoder.encode(member.getPassword()))
                .nickname(member.getNickname())
                .email(member.getEmail())
                .mobile(member.getMobile())
                .createdAt(Date.from(Instant.now()))
                .role(role)
                .build();

        memberMapper.insertMember(siteUser);
        return siteUser.getMemberId();
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
        MemberDto member = Optional.ofNullable(memberMapper.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
       log.info("유저 찾기 = {}", member.getUsername());
       if(passwordEncoder.matches(password, member.getPassword())) {
           memberWithdrawalMapper.insertMemberWithdrawal(member.getUsername());
           memberMapper.deleteMember(member.getUsername());
           return true;
       }
       return false;
    }

    @Transactional
    public MemberDto findByUsername(String username) {
        String name = Optional.ofNullable(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
        return memberMapper.findByUsername(name);
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(String username, PasswordChangeDto passwordChangeDto) {
        MemberDto member = memberMapper.findByUsername(username);
        if(member == null || !passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), member.getPassword())) {
            throw new InvalidCurrentPasswordException("InvalidPassword", "현재비밀번호가 일치하지 않습니다.");
        }
        if (passwordChangeDto.getNewPassword().equals(passwordChangeDto.getCurrentPassword())) {
            throw new InvalidCurrentPasswordException("SameAsOld", "새 비밀번호는 기존 비밀번호와 달라야 합니다.");
        }
        if(!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("NotMatchPassword", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(passwordChangeDto.getNewPassword());
        memberMapper.updatePassword(username, encodedPassword);
    }

    //아이디 찾기
    @Transactional(readOnly = true)
    public String findByUsernameEmail(FindMemberDto findMemberDto) {
        return Optional.ofNullable(memberMapper.findUsernameByEmail(findMemberDto.getEmail()))
                                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

    }

    //닉네임 변경
    @Transactional
    public void updateNickname(String username, String nickname) {
        if(memberMapper.findByUsername(username) == null) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
        //중복 검사
        if(nicknameDuplicate(nickname)) {
            throw new CustomException("isNicknameDuplicate","이미 사용중인 닉네임입니다.");
        }

        memberMapper.updateNickname(username, nickname);
    }

    //주소 변경


    //사용자명 중복 검사
    public boolean usernameDuplicate(String username) {
        return memberMapper.existsByUsername(username);
    }

    //이메일 중복 검사
    public boolean emailDuplicate(String email) {
        return memberMapper.existsByEmail(email);
    }

    //닉네임 중복 검사
    public boolean nicknameDuplicate(String nickname) {
        return memberMapper.existsByNickname(nickname);
    }


}
