package com.travelplanner.v2.domain.user.controller;

import com.travelplanner.v2.domain.user.cache.UserInfoService;
import com.travelplanner.v2.global.util.RedisUtil;
import com.travelplanner.v2.global.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유저 정보 반환 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
@Slf4j
public class UserInfoController {
    private final UserInfoService userInfoService;
    private final TokenUtil tokenUtil;
    private final RedisUtil redisUtil;

    @Operation(summary = "유저 정보 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 반환 성공"),
    })
    @GetMapping(value = "/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(tokenUtil.getJWTTokenFromHeader(request));
        String userInfo = redisUtil.getData("userInfo::" + userId);
        if (userInfo == null) {
            userInfoService.cacheUserInfo();
            userInfo = redisUtil.getData("userInfo::" + userId);
        }
        return ResponseEntity.ok().body(userInfo);
    }

    @Operation(summary = "유저 정보 갱신")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 갱신 성공"),
    })
    @GetMapping(value = "/me/reload")
    public ResponseEntity<?> reloadUserInfo(HttpServletRequest request) {
        String userId = tokenUtil.getUserIdFromToken(tokenUtil.getJWTTokenFromHeader(request));
        String userInfo = redisUtil.getData("userInfo::" + userId);
        if (userInfo == null) {
            userInfoService.reloadUserInfo();
            userInfo = redisUtil.getData("userInfo::" + userId);
        }
        return ResponseEntity.ok().body(userInfo);
    }
}
