package com.travelplanner.v2.domain.auth.userInfo.cache;

import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.AuthUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public UserInfoService(UserRepository userRepository, AuthUtil authUtil) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }

    @Cacheable(value = "userInfo", key = "#userId")
    public User getUserById() {
        Long userId = authUtil.getLoginUserIndex();
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
    }
}
