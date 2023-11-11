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

    @Cacheable(value = "userInfo", key="#p0")
    public UserDTO getUserById() {
        Long userId = authUtil.getLoginUserIndex();
        return userRepository.findById(userId)
                .map(this::toDTO)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
    }

    @CachePut(value = "userInfo", key="#p0")
    public UserDTO reloadUser() {
        Long userId = authUtil.getLoginUserIndex();
        return userRepository.findById(userId)
                .map(this::toDTO)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
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
