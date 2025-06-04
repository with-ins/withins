package com.withins.api.auth.auth2.converter;

import com.withins.core.user.entity.Provider;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultOAuth2User {
    private final Provider provider;
    private final String providerId;
    private final String name;

}
