package com.withins.core.user.dto;

import com.withins.core.user.entity.Role;

public record UserInfoResponse(
    String nickname,
    Role role
) { }
