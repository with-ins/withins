package com.withins.api.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private boolean authenticated;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String role;
}