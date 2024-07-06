package com.moz1mozi.mybatis.user.service;

import com.moz1mozi.mybatis.common.exception.CustomException;
import com.moz1mozi.mybatis.common.exception.InvalidCurrentPasswordException;
import com.moz1mozi.mybatis.common.exception.PasswordsDoNotMatchException;
import com.moz1mozi.mybatis.image.service.ImageService;
import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.*;
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

import static com.moz1mozi.mybatis.user.utils.MemberUtils.mapStringToRole;
import static com.moz1mozi.mybatis.user.utils.MemberUtils.validateMemberData;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Transactional
    public Long insertMember(UserDto user, MultipartFile profileImage) throws IOException {
        validateMemberData(user, userMapper, true);

        String profileImageUrl = null;
        if(profileImage != null && !profileImage.isEmpty()) {
           String storedFileName = imageService.storeProfileImage(profileImage);
           profileImageUrl = "/members/" + storedFileName;
        }

        Role role = mapStringToRole(user.getUserRole().getDisplayName());
        log.info("role ={}", role);
        UserDto siteUser = UserDto.builder()
                .userName(user.getUserName())
                .userPw(passwordEncoder.encode(user.getUserPw()))
                .userNickname(user.getUserNickname())
                .userEmail(user.getUserEmail())
                .userMobile(user.getUserMobile())
                .userProfileImagePath(profileImageUrl)
                .userCreatedAt(Date.from(Instant.now()))
                .userRole(role)
                .build();

        userMapper.insertUser(siteUser);
        return siteUser.getUserId();
    }

    @Transactional
    public boolean deleteMember(String username, String password) {
        UserDto user = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
       log.info("유저 찾기 = {}", user.getUserName());
       if(passwordEncoder.matches(password, user.getUserPw())) {
           userMapper.deleteUser(user.getUserName());
           return true;
       }
       return false;
    }

    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        return userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    public UserDto getEmail(String email) {
        return userMapper.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    public boolean checkUserExists(String nickname, String email, String username) {
        if(username != null) {
            //username 제공된 경우
            return userMapper.findByNicknameAndEmailAndUsername(nickname, email, username).isPresent();
        } else {
            return userMapper.findByNicknameAndEmail(nickname, email).isPresent();
        }
    }

    @Transactional(readOnly = true)
    public Long getMemberIdByUsername(String username) {
        return Optional.ofNullable(userMapper.findByUserIdByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public UserDto getMemberId(Long memberId) {
        return userMapper.findByUserId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Long> getMemberAddress(Long memberId) {
        return Optional.ofNullable(userMapper.findUserIdByAddressId(memberId))
                .orElseThrow(() -> new CustomException("addressNotFound","배송지가 존재하지 않습니다."));
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(String username, PasswordChangeDto passwordChangeDto) {
        UserDto user = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        validatePassword(passwordChangeDto, user);

        String encodedPassword = passwordEncoder.encode(passwordChangeDto.getNewPassword());
        userMapper.updatePassword(username, encodedPassword);
    }

    private void validatePassword(PasswordChangeDto passwordChangeDto, UserDto user) {
        if(!passwordEncoder.matches(passwordChangeDto.getCurrentPassword(), user.getUserPw())) {
            throw new InvalidCurrentPasswordException("invalidCurrentPassword", "현재비밀번호가 일치하지 않습니다.");
        }

        if (passwordChangeDto.getNewPassword().equals(passwordChangeDto.getCurrentPassword())) {
            throw new InvalidCurrentPasswordException("sameAsOld", "새 비밀번호는 기존 비밀번호와 달라야 합니다.");
        }
        if(!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("NotMatchPassword", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }
    }

    //아이디 찾기
    @Transactional(readOnly = true)
    public String findByUsernameEmail(FindUserDto findMemberDto) {
        return Optional.ofNullable(userMapper.findUsernameByEmail(findMemberDto.getUserEmail()))
                                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

    }

    //닉네임 변경
    @Transactional
    public void updateNickname(String username, String nickname) {
        if(userMapper.findByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
        }
        //중복 검사
        if(nicknameDuplicate(nickname)) {
            throw new CustomException("isNicknameDuplicate","이미 사용중인 닉네임입니다.");
        }

        userMapper.updateNickname(username, nickname);
    }

    //프로필 이미지 변경
    @Transactional
    public void updateProfileImage(String username, MultipartFile profileImage) throws IOException {
        UserDto existingMember = userMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        if(profileImage != null && !profileImage.isEmpty()) {
            String storedFilename = imageService.storeProfileImage(profileImage);
            String storedUrl = "/members/" + storedFilename;
            existingMember.setUserProfileImagePath(storedUrl);
            userMapper.updateProfileImage(existingMember.getUserName(), storedUrl);
        }
    }

    //사용자명 중복 검사
    public boolean usernameDuplicate(String username) {
        return userMapper.existsByUsername(username);
    }

    //이메일 중복 검사
    public boolean emailDuplicate(String email) {
        return userMapper.existsByEmail(email);
    }

    //닉네임 중복 검사
    public boolean nicknameDuplicate(String nickname) {
        return userMapper.existsByNickname(nickname);
    }


}
