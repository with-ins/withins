package com.withins.core.user.component;

import com.withins.core.exception.EntityNotFoundException;
import com.withins.core.user.entity.User;
import com.withins.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserReader {

    private final UserRepository userRepository;

    public Optional<User> readByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    public User read(final Long userId) {
        log.debug("유저 조회 실행 - id={}", userId);

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        log.debug("유저 조회 완료 - id={}, name={}", user.getId(), user.getNickname());

        return user;
    }
}
