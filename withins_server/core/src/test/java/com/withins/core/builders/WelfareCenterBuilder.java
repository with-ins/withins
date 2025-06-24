package com.withins.core.builders;

import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WelfareCenterBuilder {

    private Long id;
    private String name = "테스트 복지관";
    private String email = "test@example.com";
    private String address = "서울시 강남구 테스트로 123";
    private String url = "https://example.com";
    private KoreanRegion region = KoreanRegion.SEOUL;

    public static WelfareCenterBuilder WelfareCenter() {
        return new WelfareCenterBuilder();
    }

    public WelfareCenter build() {
        return WelfareCenter.builder()
            .id(this.id)
            .name(this.name)
            .email(this.email)
            .address(this.address)
            .url(this.url)
            .region(this.region)
            .build();
    }
}