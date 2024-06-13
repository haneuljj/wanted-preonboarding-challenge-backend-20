package com.example.wantedmarket.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
public class Members {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_pwd", nullable = false)
    private String memberPwd;

//    @Builder.Default
    private String roles;

    public List<String> getRoleList() {
        return Arrays.asList(roles.split(","));
    }
}
