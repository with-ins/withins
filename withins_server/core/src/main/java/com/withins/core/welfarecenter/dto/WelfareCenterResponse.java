package com.withins.core.welfarecenter.dto;

import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WelfareCenterResponse {

    private final String name;
    private final KoreanRegion region;

    public static WelfareCenterResponse of(WelfareCenter welfareCenter) {
        return new WelfareCenterResponse(welfareCenter.getName(), welfareCenter.getRegion());
    }
}
