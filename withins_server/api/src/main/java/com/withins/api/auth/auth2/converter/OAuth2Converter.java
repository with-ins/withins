package com.withins.api.auth.auth2.converter;

import com.withins.core.user.entity.Provider;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Random;

public sealed interface OAuth2Converter permits
    OAuth2KakaoConverter {

    DefaultOAuth2User convert(OAuth2User user);


    static OAuth2Converter of(Provider provider) {
        if (provider == Provider.KAKAO) {
            return new OAuth2KakaoConverter();
        }
        throw new OAuth2AuthenticationException(new OAuth2Error("unsupported_provider", "지원하지 않는 프로바이더입니다.", ""));
    }

    default String generateRandomNickname(Provider provider) {
        return provider.name() + "_" + new Random().nextInt(100000);
    }
}
