package com.travelplanner.v2.global.jwt;

import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ApiExceptionResponse;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.CookieUtil;
import com.travelplanner.v2.global.util.RedisUtil;
import com.travelplanner.v2.global.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

@Tag(name = "Jwt Token", description = "토큰 재발행 API")
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class JwtController {

    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @Operation(summary = "accessToken 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "accessToken 재발급 성공"),
            @ApiResponse(responseCode = "403", description = "리프레시 토큰이 만료되었습니다.",
                    content = @Content(schema = @Schema(implementation = ApiExceptionResponse.class))),
    })
    @GetMapping("/token")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = cookieUtil.getCookie(request, "refreshToken");
        if (refreshTokenCookie == null) { // 쿠키가 존재하지 않는 경우
            throw new ApiException(ErrorType.REFRESH_TOKEN_DOES_NOT_EXIST);
        }

        String refreshToken = refreshTokenCookie.getValue();
        log.info("refreshToken: " + refreshToken);
        String userId = tokenUtil.getUserIdFromToken(refreshToken);
        log.info("userId: " + userId);

        if (redisUtil.getData(userId) == null) { // 리프레시 토큰이 만료되어 레디스에서 사라진 경우
            throw new ApiException(ErrorType.REFRESH_TOKEN_EXPIRED);
        }

        // 어세스 토큰 재발급
        String newAccessToken;
        newAccessToken = tokenUtil.refreshAccessToken(refreshToken); // 만약 리프레시 토큰이 만료상태라면 403 에러를 반환.

        // 헤더에 추가
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", newAccessToken);

        tokenUtil.getAuthenticationFromToken(newAccessToken);
        return ResponseEntity.ok().headers(responseHeaders).build();
    }
}