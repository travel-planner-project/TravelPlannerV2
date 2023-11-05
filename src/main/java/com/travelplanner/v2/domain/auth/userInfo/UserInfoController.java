package com.travelplanner.v2.domain.auth.userInfo;

import com.travelplanner.v2.domain.auth.userInfo.cache.UserInfoService;
import com.travelplanner.v2.domain.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "유저 정보 반환 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {
    private final UserInfoService userInfoService;

    @Operation(summary = "유저 정보 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 반환 성공"),
    })
    @PostMapping("")
    public ResponseEntity<User> getUserInfo() {
        User user = userInfoService.getUserById();
        return ResponseEntity.ok().body(user);
    }
}
