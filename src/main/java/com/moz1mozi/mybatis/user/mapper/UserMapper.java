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
    Long insertMember(UserDto member);

    // 회원 정보 조회
    List<UserInfoDto> selectMemberInfo();

    Long deleteMember(String username);

    Optional<UserDto> findByUsername(String username);


    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findByMemberId(Long memberId);

    Long findByMemberIdByUsername(String username);

    List<Long> findMemberIdByAddressId(Long memberId);

    // 닉네임와 이메일로 해당하는 회원의 정보 찾기
    Optional<FindUserDto> findByNicknameAndEmail(@Param("nickname")String nickname, @Param("email") String email);

    // 닉네임, 이메일, 아이디에 해당하는 회원의 정보 찾기
    Optional<FindUserDto> findByNicknameAndEmailAndUsername(@Param("nickname") String nickname, @Param("email") String email, @Param("username") String username);

    // 이메일로 아이디(username) 찾기
    String findUsernameByEmail(String email);

    // 비밀번호 변경
    void updatePassword(@Param("username")String username, @Param("password")String password);

    // 닉네임 변경
    void updateNickname(@Param("username")String username, @Param("nickname")String nickname);

    // 프로필 이미지 변경
    void updateProfileImage(@Param("username") String username, @Param("profileImagePath")String profileImage);

    // 중복 검사
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByNickname(String nickname);
}
