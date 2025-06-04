package com.withins.core.user.entity;

public enum Provider {

    KAKAO;

    public static Provider findByProvider(String clientName) {
        return Provider.valueOf(clientName.toUpperCase());
    }

}