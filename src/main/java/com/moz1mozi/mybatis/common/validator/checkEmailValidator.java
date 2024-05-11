package com.moz1mozi.mybatis.common.validator;

import com.moz1mozi.mybatis.member.dao.MemberMapper;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class checkEmailValidator extends AbstractValidator<MemberDto>{
    private final MemberMapper memberMapper;
    @Override
    protected void doValidate(MemberDto memberDto, Errors errors) {
        if(memberMapper.existsByEmail(memberDto.getEmail())) {
            errors.rejectValue("email", "email.duplicate", "이미 사용중인 이메일입니다.");
        }
    }


}
