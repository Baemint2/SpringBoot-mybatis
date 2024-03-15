package com.moz1mozi.mybatis.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSecurityService implements UserDetailsService {

    private final MemberDao memberDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberDto byUsername = Optional.ofNullable(memberDao.findByRoleWithUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        log.info("username = {}", byUsername.getUsername());
        log.info("password = {}" , byUsername.getPassword());

        GrantedAuthority authority = new SimpleGrantedAuthority(byUsername.getRole().getDisplayName());

        log.info("authority = {}", authority);
        List<GrantedAuthority> authorities = Collections.singletonList(authority);
        return new User(byUsername.getUsername(), byUsername.getPassword(), authorities);
    }

}
