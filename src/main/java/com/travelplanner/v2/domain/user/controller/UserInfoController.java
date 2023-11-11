package com.travelplanner.v2.domain.user.controller;

import com.travelplanner.v2.domain.user.cache.UserDTO;
import com.travelplanner.v2.domain.user.cache.UserInfoService;
import com.travelplanner.v2.domain.user.domain.User;
import com.travelplanner.v2.domain.user.dto.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "유저 정보 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 반환 성공",
                    content = @Content(schema = @Schema(implementation = UserDTO.class)))
    })
    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> getUserInfo() {
        UserDTO user = userInfoService.getUserById();
        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "유저 정보 갱신")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 갱신 성공",
                    content = @Content(schema = @Schema(implementation = UserDTO.class)))

    })
    @GetMapping(value = "/me/reload")
    public ResponseEntity<UserDTO> reloadUserInfo() {
        UserDTO user = userInfoService.reloadUser();
        return ResponseEntity.ok().body(user);
    }
}
