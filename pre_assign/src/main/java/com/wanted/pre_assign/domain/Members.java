package com.wanted.pre_assign.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Members {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_name")
    private String memberName;
    
    @OneToMany(mappedBy = "members", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Products> productsList = new ArrayList<>();

    @OneToMany(mappedBy = "members", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Orders> membersList = new ArrayList<>();
    
}
