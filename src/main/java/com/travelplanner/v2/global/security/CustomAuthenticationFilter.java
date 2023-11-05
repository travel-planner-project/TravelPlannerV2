package com.travelplanner.v2.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelplanner.v2.domain.auth.local.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication (final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken authenticationToken;

        try {
            final LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            log.info("====================================================");
            log.info("사용자 아이디: " + loginRequest.getEmail() + " 비밀번호: " + loginRequest.getPassword());
            log.info("====================================================");

            authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        } catch (IOException exception) {
            throw new org.springframework.security.authentication.AuthenticationServiceException("사용자 인증에 실패하였습니다.", exception);
        }

        setDetails(request, authenticationToken);

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}