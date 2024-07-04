package com.moz1mozi.mybatis.user.dto;

import lombok.*;


@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberShippingAddressDto {

    private Long memberId;

    private String nickname;
}
