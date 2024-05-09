package com.moz1mozi.mybatis.email.mapper;

import com.moz1mozi.mybatis.email.dto.EmailVerificationDto;
import jakarta.validation.constraints.Email;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EmailVerificationMapper {

    @Select("SELECT EMAIL, VERIFICATION_CODE, EXPIRY_DATE_TIME FROM EMAIL_VERIFICATION_T WHERE EMAIL = #{email}")
    EmailVerificationDto findByEmail(@Param("email") String email);

    void insertVerification(EmailVerificationDto emailVerificationDto);

    @Delete("DELETE FROM EMAIL_VERIFICATION_T WHERE EMAIL = #{email}")
    void delete(EmailVerificationDto emailVerificationDto);

    @Delete("DELETE FROM EMAIL_VERIFICATION_T WHERE EXPIRY_DATE_TIME <= #{dateTime}")
    int deleteExpiredVerifications(LocalDateTime dateTime);
}
