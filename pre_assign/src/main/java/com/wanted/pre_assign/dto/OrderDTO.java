package com.wanted.pre_assign.dto;

import java.time.LocalDate;

import com.wanted.pre_assign.domain.Members;
import com.wanted.pre_assign.domain.Orders;
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
public class OrderDTO {
    private Long orderId;
    private Long memberId;
    private Long sellerId;
    private Long productId;
    private String orderState;
    private LocalDate orderDate;

    public static OrderDTO toDTO(Orders orders, Long memberId, Long sellerId, Long productId) {
        return OrderDTO.builder()
                .orderId(orders.getOrderId())
                .orderState(orders.getOrderState())
                .orderDate(orders.getOrderDate())
                .memberId(memberId)
                .sellerId(sellerId)
                .productId(productId)
                .build();
    }
}
