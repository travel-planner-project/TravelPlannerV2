package com.travelplanner.v2.domain.user.controller;

import com.travelplanner.v2.domain.user.UserService;
import com.travelplanner.v2.domain.user.dto.request.PasswordRequest;
import com.travelplanner.v2.domain.user.dto.request.ProfileImageUpdateRequest;
import com.travelplanner.v2.domain.user.dto.response.ProfileResponse;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ApiExceptionResponse;
import com.travelplanner.v2.global.exception.ErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(summary = "프로필 상세")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공",
                    content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
    })
    @GetMapping(value = "/{userId}")
    public ResponseEntity<ProfileResponse> findUserProfile(
            @Parameter(name = "userId", description = "유저 인덱스", in = ParameterIn.PATH) // swagger
            @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @Operation(summary = "프로필 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            @ApiResponse(responseCode = "403", description = "로그인한 유저와 프로필 유저가 같지 않은 경우",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "특정 유저를 찾을 수 없는 경우",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
    })
    @PatchMapping(value = "/me")
    public void updateUserProfileImage(@Parameter(name = "profileUpdateRequest", description = "프로필 수정 요청", in = ParameterIn.QUERY) // swagger
                                           @RequestPart ProfileImageUpdateRequest request,
                                           @Parameter(name = "profileImg", description = "프로필 이미지", in = ParameterIn.QUERY) // swagger
                                           @RequestPart MultipartFile multipartFile) throws Exception {
        userService.updateUserProfileImage(request, multipartFile);
    }

    @Operation(summary = "회원 수정 : 비밀번호 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 일치 확인"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않는 경우",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
    })
    @PostMapping(value = "/verify")
    public boolean checkUserPassword(@RequestBody PasswordRequest request) {
        return userService.checkPassword(request);
    }

    @Operation(summary = "회원 수정 : 비밀번호 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료"),
            @ApiResponse(responseCode = "403", description = "권한이 부족하여 접근할 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
    })
    @PatchMapping(value = "/change")
    public void updateUserPassword(@RequestBody PasswordRequest request) {
        userService.updatePassword(request);
    }


    @Operation(summary = "회원 수정 : 회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 완료"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않는 경우",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "권한이 부족하여 접근할 수 없습니다",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "유저가 존재하지 않습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
    })
    @DeleteMapping(value = "/delete")
    public void deleteUser(@RequestBody PasswordRequest request) throws ApiException {
        if (userService.checkPassword(request)) {
            userService.deleteUser();
        } else {
            throw new ApiException(ErrorType.CHECK_PASSWORD_AGAIN);
        }
    }
}
