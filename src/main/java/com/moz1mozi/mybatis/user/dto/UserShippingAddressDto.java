package com.moz1mozi.mybatis.user.dto;

import lombok.*;


@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserShippingAddressDto {

    private Long userId;

    private String nickname;
}
