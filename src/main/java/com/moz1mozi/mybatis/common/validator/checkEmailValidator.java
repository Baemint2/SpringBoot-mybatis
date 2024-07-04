package com.moz1mozi.mybatis.common.validator;

import com.moz1mozi.mybatis.user.mapper.UserMapper;
import com.moz1mozi.mybatis.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class checkEmailValidator extends AbstractValidator<UserDto>{
    private final UserMapper userMapper;
    @Override
    protected void doValidate(UserDto userDto, Errors errors) {
        if(userMapper.existsByEmail(userDto.getUserEmail())) {
            errors.rejectValue("email", "email.duplicate", "이미 사용중인 이메일입니다.");
        }
    }


}
