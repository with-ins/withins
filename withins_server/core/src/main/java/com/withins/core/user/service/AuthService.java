package com.withins.core.user.service;

import com.withins.core.user.component.UserReader;
import com.withins.core.user.dto.UserAuthToken;
import com.withins.core.user.entity.OrgUser;
import com.withins.core.user.entity.SocialUser;
import com.withins.core.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserReader userReader;

    @Transactional(readOnly = true)
    public UserAuthToken getUserAuthToken(Long userId) {
        User user = userReader.read(userId);
        return new UserAuthToken(userId, getProvider(user), user.getUsername(), List.of(user.getRole()));
    }

    private String getProvider(User user) {
        return switch (user) {
            case OrgUser orgUser -> "LOCAL";
            case SocialUser socialUser -> socialUser.getProvider().name();
            default -> throw new IllegalStateException("Unexpected value: " + user);
        };
    }

}
