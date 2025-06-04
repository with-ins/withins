package com.withins.api.controller;

import com.withins.api.auth.jwt.CustomJwtToken;
import com.withins.api.auth.jwt.JwtTokenProvider;
import com.withins.api.auth.jwt.annotation.JwtToken;
import com.withins.core.user.dto.UserAuthToken;
import com.withins.core.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@JwtToken CustomJwtToken jwtToken, HttpServletResponse response) throws IOException {
        UserAuthToken userAuthToken = authService.getUserAuthToken(jwtToken.getUserId());
        jwtTokenProvider.refresh(userAuthToken, response);
        return ResponseEntity.ok(null);
    }

}
