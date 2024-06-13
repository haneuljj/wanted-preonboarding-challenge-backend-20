package com.example.wantedmarket.member.service;

import com.example.wantedmarket.auth.JwtToken;
import com.example.wantedmarket.auth.JwtTokenProvider;
import com.example.wantedmarket.member.domain.Members;
import com.example.wantedmarket.member.dto.MemberRequest;
import com.example.wantedmarket.member.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MembersRepository membersRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtToken login(MemberRequest memberRequest) {

        String memberId = memberRequest.getMemberId();
        String memberPwd = memberRequest.getMemberPwd();

        // 1. memberId + memberPwd로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(memberId, memberPwd);

        // 2. 요청된 Member에 대한 검증 진행 -> UserDetailsService의 loadUserByUsername()으로
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            log.debug("인증 성공 memberId: {}", memberId);
        } catch (Exception e) {
            log.error("인증 실패 memberId: {}", memberId, e);
            throw e;
        }

        // 3. 인증 정보 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        log.debug("JWT토큰 생성 memberId: {}", memberId);

        return jwtToken;
    }

    public void join(MemberRequest memberRequest) {
        if (membersRepository.existsById(memberRequest.getMemberId())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        String encodedPassword = passwordEncoder.encode(memberRequest.getMemberPwd());

        Members member = Members.builder()
                .memberId(memberRequest.getMemberId())
                .memberPwd(encodedPassword)
                .roles("USER")
                .build();

        membersRepository.save(member);
    }
}
