package com.withins.core.user.component;

import com.withins.core.signup.RequestSignup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SignupAuthenticator {

    private final Map<String, SignupHolder> requestSignupMap = new ConcurrentHashMap<>();
    private static final int EXPIRATION_TIME = 60 * 60; // 1시간

    public String registry(RequestSignup requestSignup) {
        String uuid = UUID.randomUUID().toString();
        requestSignupMap.put(uuid, SignupHolder.of(requestSignup));
        return uuid;
    }

    private void clearExpired() {
        Date date = new Date();
        
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class SignupHolder {
        private final Date expiration;
        private final RequestSignup requestSignup;

        static SignupHolder of(RequestSignup signup) {
            return new SignupHolder(
                new Date(System.currentTimeMillis() + EXPIRATION_TIME),
                signup
            );
        }
    }
}
