package com.withins.core.user.component;

import com.withins.core.user.entity.Role;
import com.withins.core.user.entity.User;
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
                User.builder()
                        .socialUserId(socialUserId)
                        .nickname(nickname)
                        .role(Role.USER)
                        .build()
        ).getId();
    }
}
