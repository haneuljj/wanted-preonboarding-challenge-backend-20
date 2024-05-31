package com.wanted.pre_assign.dto;

import com.wanted.pre_assign.domain.Members;
import com.wanted.pre_assign.domain.Products;

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
public class ProductDTO {
    private Long productId;
    private Long memberId;
    private String productName;
    private int productPrice;
    private String reserveState;

    public static ProductDTO toDTO(Products product, Long memberId) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .reserveState(product.getReserveState())
                .memberId(memberId)
                .build();
    }
}
