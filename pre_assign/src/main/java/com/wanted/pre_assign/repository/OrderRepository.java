package com.wanted.pre_assign.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanted.pre_assign.domain.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long>{
    
}
