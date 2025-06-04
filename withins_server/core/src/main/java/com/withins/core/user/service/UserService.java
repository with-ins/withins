package com.withins.core.user.service;

import com.withins.core.user.component.UserReader;
import com.withins.core.user.component.UserWriter;
import com.withins.core.user.dto.UserInfoResponse;
import com.withins.core.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    @Transactional
    //TODO AOP를 통한 회원가입 모니터링 구축 필요
    public User saveOrGet(final String username, Supplier<? extends User> orElseGet) {
        return userReader.readByUsername(username)
                .orElseGet(() -> {
                    final User user = userWriter.save(orElseGet.get());
                    log.info("유저 회원가입 완료 - userId={}", user.getId());
                    return user;
                });
    }

    public Optional<User> readByUsername(final String username) {
        return userReader.readByUsername(username);
    }

    public UserInfoResponse readUserInfo(Long memberId) {
        User user = userReader.read(memberId);
        return new UserInfoResponse(user.getNickname(), user.getRole());
    }
}
