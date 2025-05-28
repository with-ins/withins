package com.withins.core.user.service;

import com.withins.core.user.component.UserReader;
import com.withins.core.user.component.UserWriter;
import com.withins.core.user.entity.Role;
import com.withins.core.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    @Transactional
    //TODO AOP를 통한 회원가입 모니터링 구축 필요
    public Long saveOrGet(final String idFromIdToken, final String nickname) {
        return userReader.readBy(idFromIdToken)
                .map(User::getId)
                .orElseGet(() -> {
                    final Long userId = userWriter.save(idFromIdToken, nickname);
                    log.info("유저 회원가입 완료 - userId={}", userId);
                    return userId;
                });
    }

    public Role getRole(final Long userId) {
        return userReader.readRole(userId);
    }
}
