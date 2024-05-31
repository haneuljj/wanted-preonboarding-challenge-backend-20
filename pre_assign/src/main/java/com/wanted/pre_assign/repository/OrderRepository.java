package com.wanted.pre_assign.repository;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanted.pre_assign.domain.Members;
import com.wanted.pre_assign.domain.Orders;
import com.wanted.pre_assign.domain.Products;

public interface OrderRepository extends JpaRepository<Orders, Long>{

    Optional<Orders> findByProducts(Products product);

    List<Orders> findAllByMembers(Members member);

}
