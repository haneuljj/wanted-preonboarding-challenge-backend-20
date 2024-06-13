package com.example.wantedmarket.member.repository;

import com.example.wantedmarket.member.domain.Members;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembersRepository extends JpaRepository<Members, String> {
    Optional<Members> findByMemberId(String memberId);
}
