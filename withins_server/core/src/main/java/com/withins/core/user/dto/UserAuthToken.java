package com.withins.core.user.dto;

import com.withins.core.user.entity.Role;

import java.util.List;

public record UserAuthToken(Long userId,
                            String provider,
                            String providerId,
                            List<Role> roles
) { }
