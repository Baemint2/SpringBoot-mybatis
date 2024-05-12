package com.moz1mozi.mybatis.member.service;

import com.moz1mozi.mybatis.common.config.IsAdmin;
import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.email.service.EmailService;
import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import com.moz1mozi.mybatis.member.dto.MemberInfoDto;
import com.moz1mozi.mybatis.member.dto.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.moz1mozi.mybatis.common.utils.RandomCodeUtils.generateTempPassword;
import static com.moz1mozi.mybatis.member.utils.MemberUtils.mapStringToRole;
import static com.moz1mozi.mybatis.member.utils.MemberUtils.validateMemberData;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberMapper memberMapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public Long insertMember(MemberDto member, MultipartFile profileImage) throws IOException {
        String tempPassword = generateTempPassword();
        String encodedPassword = passwordEncoder.encode(tempPassword);
        member.setPassword(encodedPassword);

        validateMemberData(member, memberMapper, false);

        String profileImageUrl = null;
        if(profileImage != null && !profileImage.isEmpty()) {
            String storedFileName = imageService.storeProfileImage(profileImage);
            profileImageUrl = "/members/" + storedFileName;
        }

        Role role = mapStringToRole(member.getRole().getDisplayName());
        log.info("role ={}", role);


        MemberDto siteUser = MemberDto.builder()
                .username(member.getUsername())
                .password(encodedPassword)
                .nickname(member.getNickname())
                .email(member.getEmail())
                .mobile(member.getMobile())
                .profileImagePath(profileImageUrl)
                .createdAt(Date.from(Instant.now()))
                .role(role)
                .build();

        memberMapper.insertMember(siteUser);

        emailService.sendTempPasswordEmail(member.getEmail(), tempPassword);
        return siteUser.getMemberId();
    }



    public List<MemberInfoDto> getMemberInfo() {
        return memberMapper.selectMemberInfo();
    }

    @IsAdmin
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
