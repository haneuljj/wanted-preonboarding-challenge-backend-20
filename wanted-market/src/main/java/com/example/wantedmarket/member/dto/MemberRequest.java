package com.example.wantedmarket.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class MemberRequest {

    private String memberId;
    private String memberPwd;
    // private String roles;
}
