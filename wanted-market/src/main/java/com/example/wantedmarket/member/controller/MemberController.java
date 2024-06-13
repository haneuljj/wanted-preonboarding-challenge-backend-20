package com.example.wantedmarket.member.controller;

import com.example.wantedmarket.auth.JwtToken;
import com.example.wantedmarket.member.dto.MemberRequest;
import com.example.wantedmarket.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public String join(@RequestBody MemberRequest memberRequest) {
        memberService.join(memberRequest);

        return "success";
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@RequestBody MemberRequest memberRequest) {

        log.info("request userId = {}, userPwd = {}", memberRequest.getMemberId(), memberRequest.getMemberPwd());

        JwtToken jwtToken = memberService.login(memberRequest);

        log.info("jwtToken accessToken = {}, refreshToken = {}"
                , jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
