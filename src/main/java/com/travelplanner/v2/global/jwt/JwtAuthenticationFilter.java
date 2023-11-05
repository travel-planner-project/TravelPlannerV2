package com.travelplanner.v2.global.jwt;

import com.travelplanner.v2.global.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (requestURI.equals("/auth/signup") ||
                requestURI.equals("/auth/login") ||
                requestURI.equals("/auth/logout") ||
                requestURI.equals("/auth/token") ||
                requestURI.startsWith("/oauth") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3") ||
                requestURI.startsWith("/favicon.ico") ||
                requestURI.startsWith("/ws") ||
                requestURI.startsWith("/feed") ||
                requestURI.startsWith("/login") ||
                (requestURI.startsWith("/planner") && method.equals("GET")) ||
                requestURI.startsWith("/password")
        ) {

            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 JWT 토큰 추출
        String authorizationHeader = request.getHeader("Authorization");
        String accessToken = null;

        if (authorizationHeader != null) {
            accessToken = authorizationHeader;
        }

        // access 토큰 유효성 검사
        tokenUtil.isValidToken(accessToken);
        // 토큰 인증
        tokenUtil.getAuthenticationFromToken(accessToken);

        filterChain.doFilter(request, response);
    }
}