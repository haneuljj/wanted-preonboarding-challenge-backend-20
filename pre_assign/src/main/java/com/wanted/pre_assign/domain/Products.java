package com.wanted.pre_assign.domain;

import java.util.ArrayList;
import java.util.List;

import com.wanted.pre_assign.dto.ProductDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity
public class Products {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "reserve_state")
    private String reserveState;

    @OneToOne(mappedBy = "products")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "member_id") 
    private Members members;
    
    public static Products toEntity(ProductDTO productDTO, Members members) {
        return Products.builder()
                .productId(productDTO.getProductId())
                .productName(productDTO.getProductName())
                .productPrice(productDTO.getProductPrice())
                .reserveState(productDTO.getReserveState())
                .members(members)
                .build();
    }

}
