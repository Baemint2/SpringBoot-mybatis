package com.moz1mozi.mybatis.user.mapper;

import com.moz1mozi.mybatis.user.dto.FindUserDto;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.dto.UserInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    Long insertUser(UserDto user);

    // 회원 정보 조회
    List<UserInfoDto> selectMemberInfo();

    Long deleteUser(String userName);

    Optional<UserDto> findByUsername(String userName);


    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findByUserId(Long userId);

    Long findByUserIdByUsername(String userName);

    List<Long> findUserIdByAddressId(Long userId);

    // 닉네임와 이메일로 해당하는 회원의 정보 찾기
    Optional<FindUserDto> findByNicknameAndEmail(@Param("userNickname")String nickname, @Param("userEmail") String email);

    // 닉네임, 이메일, 아이디에 해당하는 회원의 정보 찾기
    Optional<FindUserDto> findByNicknameAndEmailAndUsername(@Param("userNickname") String nickname, @Param("userEmail") String email, @Param("userName") String userName);

    // 이메일로 아이디(userName) 찾기
    String findUsernameByEmail(String email);

    // 비밀번호 변경
    void updatePassword(@Param("userName")String userName, @Param("userPw")String password);

    // 닉네임 변경
    void updateNickname(@Param("userName")String userName, @Param("userNickname")String nickname);

    // 프로필 이미지 변경
    void updateProfileImage(@Param("userName") String userName, @Param("userProfileImagePath")String profileImage);

    // 리프레쉬 토큰 업데이트
    void updateRefreshToken(@Param("refreshToken") String refreshToken, @Param("userId") Long userId);

    // 리프레쉬 토큰 삭제

    // 중복 검사
    boolean existsByEmail(String email);
    boolean existsByUsername(String userName);
    boolean existsByNickname(String nickname);
}
