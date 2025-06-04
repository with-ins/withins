package com.withins.api.auth.auth2.converter;

import com.withins.core.user.entity.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Random;

public sealed interface OAuth2Converter permits
    OAuth2KakaoConverter {

    DefaultOAuth2User convert(OAuth2User user);


    static OAuth2Converter of(Provider provider) {
        return switch (provider) {
            case KAKAO -> new OAuth2KakaoConverter();
            default -> throw new IllegalArgumentException("지원하지 않는 제공자입니다.");
        };
    }

    default String generateRandomNickname(Provider provider) {
        return provider.name() + "_" + new Random().nextInt(100000);
    }
}
