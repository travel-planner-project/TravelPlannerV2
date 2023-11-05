package com.travelplanner.v2.global.jwt;

import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ApiExceptionResponse;
import com.travelplanner.v2.global.exception.ErrorType;
import com.travelplanner.v2.global.util.AuthUtil;
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
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

@Tag(name = "JWT", description = "토큰 재발행 API")
@Slf4j
@RestController
@RequestMapping(value = "/api")
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
    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshTokenCookie = cookieUtil.getCookie(request, "refreshToken");
        if (refreshTokenCookie == null) { // 쿠키가 존재하지 않는 경우
            throw new ApiException(ErrorType.REFRESH_TOKEN_DOES_NOT_EXIST);
        }

        String refreshToken = refreshTokenCookie.getValue();
        String userId = tokenUtil.getUserIdFromToken(refreshToken);
        log.info("====================================================");
        log.info("refreshToken: " + refreshToken);
        log.info("userId: " + userId);
        log.info("====================================================");

        if (redisUtil.getData(userId) == null) {
            String newRefreshToken = tokenUtil.generateRefreshToken(userId);
            redisUtil.setDataExpire("refreshToken", newRefreshToken, Duration.ofDays(7));
            cookieUtil.create(refreshToken, response);
        }

        // 어세스 토큰 재발급
        String newAccessToken;
        newAccessToken = "Bearer" + tokenUtil.refreshAccessToken(refreshToken);

        // 헤더에 추가
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", newAccessToken);

        tokenUtil.getAuthenticationFromToken(newAccessToken);
        return ResponseEntity.ok().headers(responseHeaders).build();
    }
}