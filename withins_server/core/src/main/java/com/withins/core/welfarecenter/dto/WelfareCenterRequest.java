package com.withins.core.welfarecenter.dto;

import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WelfareCenterRequest {

    private final String name;
    private final KoreanRegion region;


    public static WelfareCenterRequest of(WelfareCenter welfareCenter) {
        return new WelfareCenterRequest(welfareCenter.getName(), welfareCenter.getRegion());
    }
}
