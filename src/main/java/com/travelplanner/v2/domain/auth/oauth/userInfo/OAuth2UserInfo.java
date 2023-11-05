package com.travelplanner.v2.domain.auth.oauth.userInfo;

public interface OAuth2UserInfo {
    String getProfile();
    String getEmail();
    String getName();
    String getPassword();
}