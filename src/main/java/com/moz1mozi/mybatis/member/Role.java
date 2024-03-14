package com.moz1mozi.mybatis.member;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("관리자"),
    SELLER("판매자"),
    BUYER("구매자");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}