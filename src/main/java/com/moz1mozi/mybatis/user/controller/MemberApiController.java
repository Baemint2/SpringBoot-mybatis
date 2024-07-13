package com.moz1mozi.mybatis.user.controller;

import com.moz1mozi.mybatis.email.service.AuthenticationService;
import com.moz1mozi.mybatis.email.service.EmailService;
import com.moz1mozi.mybatis.user.dto.FindUserDto;
import com.moz1mozi.mybatis.user.dto.UserDto;
import com.moz1mozi.mybatis.user.dto.MemberWithdrawalDto;
import com.moz1mozi.mybatis.user.dto.PasswordChangeDto;
import com.moz1mozi.mybatis.user.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/member")
public class MemberApiController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestPart("member") UserDto memberDto,
                                                      @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        Long userId = memberService.insertMember(memberDto, file);
        log.info("회원가입 = {}", userId);
        return ResponseEntity.ok(Map.of("message", "회원가입이 성공적으료 완료되었습니다."));
    }


    //회원 탈퇴
    @DeleteMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(@RequestBody MemberWithdrawalDto withdrawalDto,
                                        HttpServletRequest request) {
        boolean withdrawalMember = memberService.deleteMember(withdrawalDto.getUserName(), withdrawalDto.getUserPw());
        if(withdrawalMember) {
            SecurityContextHolder.clearContext();

            HttpSession session = request.getSession(false);
            if(session != null) {
                session.invalidate();
            }
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("사용자 삭제 실패: 사용자명 또는 비밀번호가 일치하지 않습니다.");
        }

    }

    @GetMapping("/info")
    public ResponseEntity<UserDto> getMemberInfo(Principal principal) {
        UserDto member = memberService.findByUsername(principal.getName());
        if(member != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // 닉네임 변경
    @PutMapping("/updateNickname")
    public ResponseEntity<?> updateNickname(@RequestParam String nickname, Principal principal) {
        String username = principal.getName();
        memberService.updateNickname(username, nickname);
        Map<String, String> response = Collections.singletonMap("nickname", nickname);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@RequestBody FindUserDto findUserDto) {
        boolean userExists = memberService.checkUserExists(findUserDto.getUserNickname(), findUserDto.getUserEmail(), findUserDto.getUserName());
        if(!userExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "일치하는 회원 정보가 없습니다."));
        }
        authenticationService.sendAndSaveVerificationCode(findUserDto.getUserEmail());
        return ResponseEntity.ok().body(Map.of("message", "인증 코드가 발송되었습니다."));
    }


    @PutMapping("/updatePassword")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordChangeDto password, HttpSession session) {
        try {
            memberService.changePassword(password.getUserName(), password);
            session.invalidate(); // 세션 종료
            return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 업데이트 되었습니다. 다시 로그인 해주세요."));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "비밀번호 변경 실패: " + ex.getMessage()));
        }
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> changeProfileImage(@RequestPart("username") String username,
                                                @RequestPart(value = "file", required = false) MultipartFile profileImage) {
        try {
            memberService.updateProfileImage(username, profileImage);
            return ResponseEntity.ok(Map.of("message", "프로필 이미지가 성공적으로 업데이트되었습니다."));
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().body(Map.of("error", "프로필 이미지 업데이트에 실패했습니다."));
        }
    }


    @GetMapping("/username/check")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam("username")String username) {
        boolean isUsernameDuplicate = memberService.usernameDuplicate(username);
        log.info("중복된 아이디 = {}", isUsernameDuplicate);
        return ResponseEntity.ok(Map.of("isUsernameDuplicate",isUsernameDuplicate));
    }

    @GetMapping("/email/check")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam("email")String email) {
        boolean isEmailDuplicate = memberService.emailDuplicate(email);
        log.info("중복된 이메일 = {}", isEmailDuplicate);
        return ResponseEntity.ok(Map.of("isEmailDuplicate",isEmailDuplicate));
    }

    @GetMapping("/nickname/check")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam("nickname")String nickname) {
        boolean isNicknameDuplicate = memberService.nicknameDuplicate(nickname);
        log.info("중복된 닉네임 = {}", isNicknameDuplicate);
        return ResponseEntity.ok(Map.of("isNicknameDuplicate", isNicknameDuplicate));
    }

}
