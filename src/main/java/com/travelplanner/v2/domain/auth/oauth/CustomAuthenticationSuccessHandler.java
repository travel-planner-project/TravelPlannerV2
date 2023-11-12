package com.travelplanner.v2.domain.auth.oauth;

import com.travelplanner.v2.domain.auth.oauth.userInfo.GoogleUserInfo;
import com.travelplanner.v2.domain.auth.oauth.userInfo.KakaoUserInfo;
import com.travelplanner.v2.domain.auth.oauth.userInfo.NaverUserInfo;
import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.global.util.CookieUtil;
import com.travelplanner.v2.global.util.RedisUtil;
import com.travelplanner.v2.global.util.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String provider = customOAuth2User.getUser().getProvider();
        String email = null;

        if (provider.equals("kakao")) {
            KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(customOAuth2User.getAttributes());
            email = kakaoUserInfo.getEmail();

        } else if (provider.equals("google")) {
            GoogleUserInfo googleUserInfo = new GoogleUserInfo(customOAuth2User.getAttributes());
            email = googleUserInfo.getEmail();

        } else if (provider.equals("naver")) {
            NaverUserInfo naverUserInfo = new NaverUserInfo(customOAuth2User.getAttributes());
            email = naverUserInfo.getEmail();
        }

        if(response.isCommitted()) {
            log.info("====================================================");
            log.debug("Response 전송 완료");
            log.info("====================================================");
        }

        log.info("====================================================");
        log.info("소셜 로그인 성공: " + email + "소셜 타입: " + provider);
        log.info("====================================================");

        Optional<User> user = userRepository.findByEmailAndProvider(email, provider);
        String userId = String.valueOf(user.get().getId());

        // 인증이 성공했을 때, 어세스 토큰과 리프레시 토큰 발급
        String accessToken = tokenUtil.generateAccessToken(userId);
        // 어세스 토큰은 헤더에 담아서 응답으로 보냄
        response.setHeader("Authorization", "Bearer " + accessToken);

        // 리프레시 토큰을 Redis 에 저장
        if (redisUtil.getData(userId) == null) {
            String refreshToken = tokenUtil.generateRefreshToken(userId);
            // 리프레시 토큰은 쿠키에 담아서 응답으로 보냄
            cookieUtil.create(refreshToken, response);
        }

        String encodedUserId = URLEncoder.encode(userId, StandardCharsets.UTF_8);
        String encodedEmail = URLEncoder.encode(user.get().getEmail(), StandardCharsets.UTF_8);
        String encodedNickname = URLEncoder.encode(user.get().getUserNickname(), StandardCharsets.UTF_8);
        String encodedProvider = URLEncoder.encode(user.get().getProvider(), StandardCharsets.UTF_8);
        String encodedProfileImgUrl = URLEncoder.encode(user.get().getProfileImageUrl(), StandardCharsets.UTF_8);

        // 프론트엔드 페이지로 토큰과 함께 리다이렉트
        String frontendRedirectUri = "http://localhost:5173";
        String frontendRedirectUrl = String.format(
                "%s/oauth/callback?token=%s&userId=%s&email=%s&nickname=%s&provider=%s&profileImgUrl=%s",
                frontendRedirectUri, accessToken, encodedUserId, encodedEmail, encodedNickname,
                encodedProvider, encodedProfileImgUrl
        );
        response.sendRedirect(frontendRedirectUrl);
    }
}