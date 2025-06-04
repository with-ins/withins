package com.withins.api.auth.auth2.converter;

import com.withins.core.user.entity.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public final class OAuth2KakaoConverter implements OAuth2Converter {

    @Override
    public DefaultOAuth2User convert(OAuth2User user) {
        Map<String, Object> properties = user.getAttribute("properties");
        String nickname = (String) properties.get("nicknamke");
        if (nickname == null) {
            nickname = generateRandomNickname(Provider.KAKAO);
        }
        return DefaultOAuth2User.builder()
            .provider(Provider.KAKAO)
            .providerId(user.getName())
            .name(nickname)
            .build();
    }
}
