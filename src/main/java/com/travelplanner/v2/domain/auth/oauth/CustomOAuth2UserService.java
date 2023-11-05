package com.travelplanner.v2.domain.auth.oauth;

import com.travelplanner.v2.domain.auth.oauth.CustomOAuth2User;
import com.travelplanner.v2.domain.auth.oauth.userInfo.OAuth2UserInfo;
import com.travelplanner.v2.domain.auth.oauth.userInfo.OAuthUserInfoFactory;
import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.Role;
import com.travelplanner.v2.domain.user.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private @Lazy PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        log.info("====================================================");
        log.info("getAttributes : {}", oAuth2User.getAttributes());
        log.info("====================================================");

        String provider = request.getClientRegistration().getRegistrationId();

        // 어떤 소셜 로그인인지 구분
        OAuth2UserInfo oAuth2UserInfo = OAuthUserInfoFactory.getOAuthUserInfo(provider, oAuth2User.getAttributes());

        // 회원가입 유무 확인
        Optional<User> user = userRepository.findByEmailAndProvider(oAuth2UserInfo.getEmail(), provider);
        // 없다면 회원가입
        if(user.isEmpty()) {
            // 멤버 생성 및 저장
            User  newUser = User.builder()
                    .userNickname(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .provider(provider)
                    .password(passwordEncoder.encode("oauth"))
                    .role(Role.MEMBER)
                    .build();

            userRepository.save(newUser);  // 변경된 user 저장

            return new CustomOAuth2User(newUser, oAuth2User.getAttributes());
        }

        return new CustomOAuth2User(user.get(), oAuth2User.getAttributes());
    }

}