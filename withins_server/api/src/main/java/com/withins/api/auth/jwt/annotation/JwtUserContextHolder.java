package com.withins.api.auth.jwt.annotation;

import com.withins.api.auth.jwt.CustomJwtToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUserContextHolder {

    public static CustomJwtToken getJwtToken() {
        return (CustomJwtToken) SecurityContextHolder.getContext().getAuthentication();
    }
}
