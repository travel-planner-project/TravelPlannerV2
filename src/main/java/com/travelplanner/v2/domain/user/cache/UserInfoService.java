package com.travelplanner.v2.domain.user.cache;

import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Cacheable(value = "userInfo", key = "@authUtil.getLoginUserIndex().toString()")
    public UserDTO cacheUserInfo() {
        User user = authUtil.getLoginUser().get();
        return toDTO(user);
    }

    @CachePut(value = "userInfo", key="@authUtil.getLoginUserIndex().toString()")
    public UserDTO reloadUserInfo() {
        User user = authUtil.getLoginUser().get();
        return toDTO(user);
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(String.valueOf(user.getId()));
        dto.setUserNickname(user.getUserNickname());
        dto.setEmail(user.getEmail());
        dto.setProfileImageUrl(user.getProfileImageUrl());
        return dto;
    }
}
