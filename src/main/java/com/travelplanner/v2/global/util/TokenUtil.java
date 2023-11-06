package com.travelplanner.v2.global.util;

import com.travelplanner.v2.domain.user.UserRepository;
import com.travelplanner.v2.global.exception.ApiException;
import com.travelplanner.v2.global.exception.ErrorType;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUtil extends StompSessionHandlerAdapter{

    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    @Value("${secret.key}")
    private String SECRET_KEY;

    // Access 토큰 유효시간 15 분
    static final long AccessTokenValidTime = 15 * 60 * 1000L;

    public String generateAccessToken(String userId) {

        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AccessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 리프레시토큰 발행
    public String generateRefreshToken(String userId) {

        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // 저장
        redisUtil.setDataExpire(userId, refreshToken, Duration.ofDays(7));

        return refreshToken;
    }


    // 토큰의 유효성 검사
    public boolean isValidToken(String token) {
        token = token.substring(7);
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            return claims.getBody()
                    .getExpiration()
                    .after(new Date());

        } catch (ExpiredJwtException e) { // 어세스 토큰 만료
            e.printStackTrace();
            throw  e;

        } catch (Exception e) {
            throw new ApiException(ErrorType.USER_NOT_AUTHORIZED);
        }
    }


    // 어세스 토큰에서 유저 아이디 얻기
    public String getUserIdFromToken (String token) {
        String userId = Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();

        log.info("====================================================");
        log.info("유저 Id: " + userId);
        log.info("====================================================");
        return userId;
    }


    // 어세스 토큰 재발행
    public String refreshAccessToken(String refreshToken) throws ApiException {
        String userId = getUserIdFromToken(refreshToken);
        // 리프레시 토큰의 사용자 정보를 기반으로 새로운 어세스 토큰 발급
        return generateAccessToken(userId);
    }


    // 어세스 토큰을 헤더에서 추출하는 메서드
    public String getJWTTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        log.info("====================================================");
        log.info("Authorization Header: " + authorizationHeader); // 로그 추가
        log.info("====================================================");

        if (authorizationHeader != null) {
            return authorizationHeader;
        }
        return null;
    }


    // 토큰 인증과정 밟기
    public void getAuthenticationFromToken(String accessToken) {
        accessToken = accessToken.substring(7);
        Long userId = Long.valueOf(getUserIdFromToken(accessToken));
        String principal = userRepository.findById(userId).get().getEmail();
        log.info("====================================================");
        log.info("어세스 토큰: " + accessToken);
        log.info("유저 이메일: " + principal);
        log.info("====================================================");

        // JWT 토큰이 유효하면, 사용자 정보를 연결 세션에 추가
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(principal, accessToken, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    // 임시 토큰 발급
    public String generateTempToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId)); // Subject를 이메일로 설정
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis())) // 임시 토큰은 30분 동안 유효
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 토큰으로 claim에서 이메일 추출
    public String getEmailFromToken(String token) {
        String userId = Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();

        String email = userRepository.findById(Long.parseLong(userId)).get().getEmail();
        log.info("====================================================");
        log.info("유저 이메일: " + email);
        log.info("====================================================");

        return email;
    }
}