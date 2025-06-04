package com.withins.api.controller;

import com.withins.api.auth.jwt.CustomJwtToken;
import com.withins.api.auth.jwt.annotation.JwtToken;
import com.withins.core.user.dto.UserInfoResponse;
import com.withins.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/auth/user")
    public ResponseEntity<UserInfoResponse> getMemberInfo(@JwtToken CustomJwtToken jwtToken) {
        var memberInfo = userService.readUserInfo(jwtToken.getUserId());
        return ResponseEntity.ok(memberInfo);
    }


}
