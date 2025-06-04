package com.withins.core.user.component;

import com.withins.core.user.entity.Provider;
import com.withins.core.user.entity.Role;
import com.withins.core.user.entity.SocialUser;
import com.withins.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserWriter {

    private final UserRepository userRepository;

    public Long save(final String socialUserId, final String nickname) {
        return userRepository.save(
                SocialUser.builder()
                    .provider(Provider.KAKAO)
                    .providerId(socialUserId)
                    .nickname(nickname)
                    .role(Role.USER)
                    .build()
        ).getId();
    }
}
