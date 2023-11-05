package com.travelplanner.v2.domain.auth.local;

import com.travelplanner.v2.domain.auth.local.dto.LoginRequest;
import com.travelplanner.v2.domain.auth.local.dto.RegisterRequest;
import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.Role;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.CookieUtil;
import com.travelplanner.v2.global.util.RedisUtil;
import com.travelplanner.v2.global.util.TokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final AuthenticationManager authenticationManager;


    // 회원가입
    @Transactional
    public void register(RegisterRequest request) throws ApiException {

        // 이메일로 멤버 조회
        userRepository.findByEmailAndProvider(request.getEmail(), "local")
                .ifPresent(user -> {
                    throw new ApiException(ErrorType.ALREADY_EXIST_EMAIL);
                });

        if (request.getEmail() == null || request.getPassword() == null || request.getUserNickname() == null) {
            throw new ApiException(ErrorType.NULL_VALUE_EXIST);
        }

        User user = User.builder()
                .userNickname(request.getUserNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .provider("local")
                .build();

        userRepository.save(user);
    }

    // 로그인
    @Transactional
    public void login(LoginRequest request, HttpServletResponse response) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmailAndProvider(request.getEmail(), "local")
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        // 인증이 성공했을 때, 어세스 토큰과 리프레시 토큰 발급
        String accessToken = tokenUtil.generateAccessToken(String.valueOf(user.getId()));

        // 어세스 토큰은 헤더에 담아서 응답으로 보냄
        response.setHeader("Authorization", accessToken);

        if (redisUtil.getData(String.valueOf(user.getId())) == null) { // 레디스에 토큰이 저장되어 있지 않은 경우
            String refreshToken = tokenUtil.generateRefreshToken(String.valueOf(user.getId()));

            // 리프레시 토큰은 쿠키에 담아서 응답으로 보냄
            cookieUtil.create(refreshToken, response);
            redisUtil.setDataExpire(String.valueOf(user.getId()), refreshToken, Duration.ofDays(7));

        } else { // 레디스에 토큰이 저장되어 있는 경우
            String refreshToken = redisUtil.getData(String.valueOf(user.getId()));
            cookieUtil.create(refreshToken, response);
        }
    }

    // 로그아웃
    @Transactional
    public void logout(HttpServletResponse response) {
        // 어세스토큰 삭제
        response.setHeader("Authorization", "");
        // 쿠키 삭제
        cookieUtil.delete("", response);
    }
}