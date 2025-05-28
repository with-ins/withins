package com.withins.api.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OidcUserInfo {
    private String socialUserid;
    private String nickname;

    public static OidcUserInfo from(Map<String, Object> idTokenClaims) {
        return new OidcUserInfo(
                String.valueOf(idTokenClaims.get("sub")),
                String.valueOf(idTokenClaims.get("nickname"))
        );
    }
}
