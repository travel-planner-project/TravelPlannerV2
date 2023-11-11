package com.travelplanner.v2.global.security;

import com.travelplanner.v2.global.exception.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorType errorType = null;
        String requestURI = request.getRequestURI();

        if (authException instanceof InsufficientAuthenticationException) { // 인가에 실패한 경우
            setResponse(response, ErrorType.ACCESS_TOKEN_EXPIRED);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorType errorType) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        int status = Integer.parseInt(String.valueOf(errorType.getStatus()).substring(0,3));
        response.setStatus(status);

        response.getWriter().println(
                "{\"status\" : \"" +  errorType.getStatus() + "\"," +
                        "\"errorCode\" : \"" + errorType.getErrorCode() + "\"," +
                        " \"message\" : \"" + errorType.getMessage() +
                        "\"}");
    }
}
