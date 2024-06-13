package com.example.wantedmarket.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {
    // 클라이언트 요청시 JWT인증을 하기 위한 커스텀 필터
    // 클라이언트로부터 들어오는 요청에서 JWT토큰을 처리하고, 유효한 토큰인 경우 해당 토큰의 인증정보를
    // Security Context에 저장하여 인증된 요청을 처리할 수 있도록 함

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. request header에서 JWT 토큰 가져오기
        String token = resolveToken((HttpServletRequest) request);

        // 2. validateToken으로 토큰 유효성 검사

        // 유효할 경우
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 authentication 객체를 가져와 SecurityContext에 저장 => 요청을 처리하는 동안 인증 정보가 유지되도록
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    // request header에서 JWT 토큰 가져오기
    private String resolveToken(HttpServletRequest request) {
        // Authorization 헤더에서 "Bearer" 접두사로 시작하는 토큰 추출하여 반환
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
