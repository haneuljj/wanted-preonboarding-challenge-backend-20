package com.wanted.pre_assign.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanted.pre_assign.domain.Members;

public interface MemberRepository extends JpaRepository<Members, Long>{
    
}
