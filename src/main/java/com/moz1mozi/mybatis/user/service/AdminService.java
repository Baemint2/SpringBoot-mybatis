package com.moz1mozi.mybatis.user.service;

import com.moz1mozi.mybatis.common.config.IsAdmin;
import com.moz1mozi.mybatis.email.service.EmailService;
import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.dto.UserInfoDto;
import com.moz1mozi.mybatis.user.dto.Role;
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
import static com.moz1mozi.mybatis.user.utils.MemberUtils.mapStringToRole;
import static com.moz1mozi.mybatis.user.utils.MemberUtils.validateMemberData;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserMapper userMapper;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public Long insertMember(UserDto user, MultipartFile profileImage) throws IOException {
        String tempPassword = generateTempPassword();
        String encodedPassword = passwordEncoder.encode(tempPassword);
        user.setUserPw(encodedPassword);

        validateMemberData(user, userMapper, false);

        String userProfileImageUrl = null;
        if(profileImage != null && !profileImage.isEmpty()) {
            String storedFileName = imageService.storeProfileImage(profileImage);
            userProfileImageUrl = "/members/" + storedFileName;
        }

        Role role = mapStringToRole(user.getUserRole().getDisplayName());
        log.info("role ={}", role);


        UserDto siteUser = UserDto.builder()
                .userName(user.getUserName())
                .userPw(encodedPassword)
                .userNickname(user.getUserNickname())
                .userEmail(user.getUserEmail())
                .userMobile(user.getUserMobile())
                .userProfileImagePath(userProfileImageUrl)
                .userCreatedAt(Date.from(Instant.now()))
                .userRole(role)
                .build();

        userMapper.insertUser(siteUser);

        emailService.sendTempPasswordEmail(user.getUserEmail(), tempPassword);
        return siteUser.getUserId();
    }



    public List<UserInfoDto> getMemberInfo() {
        return userMapper.selectMemberInfo();
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

        userMapper.deleteUser(username);
        log.info("관리자: {} 삭제한 유저: {}", adminName, username);
    }
}
