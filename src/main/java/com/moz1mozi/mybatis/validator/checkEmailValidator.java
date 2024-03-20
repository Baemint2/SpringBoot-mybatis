package com.moz1mozi.mybatis.validator;

import com.moz1mozi.mybatis.member.dao.MemberDao;
import com.moz1mozi.mybatis.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
@RequiredArgsConstructor
public class checkEmailValidator extends AbstractValidator<MemberDto>{
    private final MemberDao memberDao;
    @Override
    protected void doValidate(MemberDto memberDto, Errors errors) {
        if(memberDao.existsByEmail(memberDto.getEmail())) {
            errors.rejectValue("email", "email.duplicate", "이미 사용중인 이메일입니다.");
        }
    }


}
