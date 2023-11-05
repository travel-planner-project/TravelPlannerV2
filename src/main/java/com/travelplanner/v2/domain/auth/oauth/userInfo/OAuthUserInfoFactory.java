package com.travelplanner.v2.domain.auth.oauth.userInfo;

import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
public class OAuthUserInfoFactory {
    public static OAuth2UserInfo getOAuthUserInfo (String provider, Map<String, Object> attributes) {

        if (provider.equals("google")) {
            log.info("====================================================");
            log.info("구글 로그인 요청");
            log.info("====================================================");

            return new GoogleUserInfo(attributes);

        } else if (provider.equals("kakao")) {
            log.info("====================================================");
            log.info("카카오 로그인 요청");
            log.info("====================================================");

            return new KakaoUserInfo(attributes);

        } else if (provider.equals("naver")) {
            log.info("====================================================");
            log.info("네이버 로그인 요청");
            log.info("====================================================");

            return new NaverUserInfo(attributes);
        }

        return null;
    }
}