package com.example.wantedmarket.member.service;

import com.example.wantedmarket.member.domain.Members;
import com.example.wantedmarket.member.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MembersRepository membersRepository;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        // log.info("=====findById: {}" , membersRepository.findByMemberId(memberId).get());

        return membersRepository.findByMemberId(memberId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }

    // 입력받은 유저의 데이터가 존재한다면 UserDetails 객체로 만들어야함
    private UserDetails createUserDetails(Members member) {
        return User.builder()
                .username(member.getMemberId())
                .password(member.getMemberPwd())
                .roles(member.getRoleList().toArray(new String[0]))
                .build();
    }
}
