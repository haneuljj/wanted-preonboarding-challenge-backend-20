package com.wanted.pre_assign.domain;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.wanted.pre_assign.dto.OrderDTO;
import com.wanted.pre_assign.dto.ProductDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
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
public class Orders {

    @SequenceGenerator(
			name="orders_seq"
			, sequenceName = "orders_seq"
			, initialValue = 1
			, allocationSize = 1)
    @Id
    @GeneratedValue(generator="orders_seq")
    @Column(name = "order_id")
    private Long orderId;

    @ColumnDefault("판매승인전")
    @Column(name = "order_state")
    private String orderState;

    @CreationTimestamp
    @Column(name = "order_date")
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "member_id") 
    private Members members;

    @OneToOne
    @JoinColumn(name = "product_id") 
    private Products products;

    public Orders(Members members, Products products) {
        this.members = members;
        this.products = products;
    }

    public static Orders toEntity(OrderDTO orderDTO, Members members, Products products) {
        return Orders.builder()
                .orderId(orderDTO.getOrderId())
                .orderState(orderDTO.getOrderState())
                .orderDate(orderDTO.getOrderDate())
                .members(members)
                .products(products)
                .build();
    }
}
