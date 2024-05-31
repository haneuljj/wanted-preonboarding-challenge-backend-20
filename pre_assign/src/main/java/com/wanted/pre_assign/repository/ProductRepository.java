package com.wanted.pre_assign.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanted.pre_assign.domain.Members;
import com.wanted.pre_assign.domain.Orders;
import com.wanted.pre_assign.domain.Products;

public interface ProductRepository extends JpaRepository<Products, Long>{

    Optional<Products> findByOrders(Orders order);

    List<Products> findAllByMembers(Members member);
    
}
