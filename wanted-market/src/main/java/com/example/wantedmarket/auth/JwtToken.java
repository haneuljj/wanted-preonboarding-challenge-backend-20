package com.example.wantedmarket.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String grantType; // JWT에 대한 인증타입
    private String accessToken;
    private String refreshToken;
}
