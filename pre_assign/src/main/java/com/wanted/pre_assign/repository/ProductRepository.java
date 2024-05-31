package com.wanted.pre_assign.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanted.pre_assign.domain.Products;

public interface ProductRepository extends JpaRepository<Products, Long>{
    
}
