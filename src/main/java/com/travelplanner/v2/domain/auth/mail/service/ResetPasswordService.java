package com.travelplanner.v2.domain.auth.mail.service;

import com.travelplanner.v2.domain.auth.mail.dto.UpdatePasswordDto;
import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.domain.user.domain.UserEditor;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.RedisUtil;
import com.travelplanner.v2.global.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenUtil tokenUtil;
    private final RedisUtil redisUtil;
    private static final String PROVIDER_LOCAL = "local";
    // 임시 토큰 생성
    public String generateTempToken(String email) {
        User user = userRepository.findByEmailAndProvider(email, PROVIDER_LOCAL)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        String tempToken = tokenUtil.generateTempToken(user.getId());  // TokenUtil에 임시 토큰 생성 메서드 추가
        redisUtil.setDataExpireWithPrefix("temp", email, tempToken, Duration.ofMinutes(30));
        String resetLink = "https://dev.travel-planner.xyz/findpassword?token=" + tempToken;
        return resetLink;
    }

    public void changePassword(UpdatePasswordDto updatePasswordDto, String tempToken) {
        if (!tokenUtil.isValidToken(tempToken)) {
            throw new ApiException(ErrorType.TOKEN_NOT_VALID);
        }

        String email = tokenUtil.getEmailFromToken(tempToken);

        // 이메일을 사용하여 멤버 찾기
        User memberToUpdate = userRepository.findByEmailAndProvider(email, PROVIDER_LOCAL)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        String newPassword = updatePasswordDto.getNewPassword();
        log.info("====================================================");
        log.info("newPassword: " + newPassword);
        log.info("====================================================");

        String encodedPassword = encoder.encode(newPassword);

        UserEditor userEditor = UserEditor.builder()
                .password(encodedPassword)
                .build();

        memberToUpdate.edit(userEditor);
        userRepository.save(memberToUpdate);
    }
}
