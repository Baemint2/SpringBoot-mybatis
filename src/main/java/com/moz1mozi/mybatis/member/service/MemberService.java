package com.moz1mozi.mybatis.member.service;

import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.common.exception.InvalidCurrentPasswordException;
import com.moz1mozi.mybatis.common.exception.PasswordsDoNotMatchException;
import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dao.MemberWithdrawalMapper;
import com.moz1mozi.mybatis.member.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final MemberWithdrawalMapper memberWithdrawalMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Transactional
    public Long insertMember(MemberDto member, MultipartFile profileImage) throws IOException {
        validateMemberData(member);

        String profileImageUrl = null;
        if(profileImage != null && !profileImage.isEmpty()) {
           String storedFileName = imageService.storeProfileImage(profileImage);
           profileImageUrl = "/members/" + storedFileName;
        }

        Role role = mapStringToRole(member.getRole().getDisplayName());
        log.info("role ={}", role);
        MemberDto siteUser = MemberDto.builder()
                .username(member.getUsername())
                .password(passwordEncoder.encode(member.getPassword()))
                .nickname(member.getNickname())
                .email(member.getEmail())
                .mobile(member.getMobile())
                .profileImagePath(profileImageUrl)
                .createdAt(Date.from(Instant.now()))
                .role(role)
                .build();

        memberMapper.insertMember(siteUser);
        return siteUser.getMemberId();
    }

    private void validateMemberData(MemberDto member) {
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
        MemberDto member = memberMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
       log.info("유저 찾기 = {}", member.getUsername());
       if(passwordEncoder.matches(password, member.getPassword())) {
           memberWithdrawalMapper.insertMemberWithdrawal(member.getUsername());
           memberMapper.deleteMember(member.getUsername());
           return true;
       }
       return false;
    }

    @Transactional(readOnly = true)
    public MemberDto findByUsername(String username) {
        return memberMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    public MemberDto getEmail(String email) {
        return memberMapper.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    public boolean checkUserExists(String nickname, String email, String username) {
        if(username != null) {
            //username 제공된 경우
            return memberMapper.findByNicknameAndEmailAndUsername(nickname, email, username).isPresent();
        } else {
            return memberMapper.findByNicknameAndEmail(nickname, email).isPresent();
        }
    }

    @Transactional(readOnly = true)
    public Long getMemberIdByUsername(String username) {
        return Optional.ofNullable(memberMapper.findByMemberIdByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public MemberDto getMemberId(Long memberId) {
        return memberMapper.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Long> getMemberAddress(Long memberId) {
        return Optional.ofNullable(memberMapper.findMemberIdByAddressId(memberId))
                .orElseThrow(() -> new CustomException("addressNotFound","배송지가 존재하지 않습니다."));
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(String username, PasswordChangeDto passwordChangeDto) {
        MemberDto member = memberMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), member.getPassword())) {
            throw new InvalidCurrentPasswordException("invalidCurrentPassword", "현재비밀번호가 일치하지 않습니다.");
        }

        if (passwordChangeDto.getNewPassword().equals(passwordChangeDto.getCurrentPassword())) {
            throw new InvalidCurrentPasswordException("sameAsOld", "새 비밀번호는 기존 비밀번호와 달라야 합니다.");
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
        if(memberMapper.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
        //중복 검사
        if(nicknameDuplicate(nickname)) {
            throw new CustomException("isNicknameDuplicate","이미 사용중인 닉네임입니다.");
        }

        memberMapper.updateNickname(username, nickname);
    }

    //프로필 이미지 변경
    @Transactional
    public void updateProfileImage(String username, MultipartFile profileImage) throws IOException {
        MemberDto existingMember = memberMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if(profileImage != null && !profileImage.isEmpty()) {
            String storedFilename = imageService.storeProfileImage(profileImage);
            String storedUrl = "/members/" + storedFilename;
            existingMember.setProfileImagePath(storedUrl);
            memberMapper.updateProfileImage(existingMember.getUsername(), storedUrl);
        }
    }

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
