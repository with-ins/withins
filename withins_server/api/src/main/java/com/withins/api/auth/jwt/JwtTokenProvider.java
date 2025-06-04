package com.withins.api.auth.jwt;

import com.withins.api.auth.JwtApiVersion;
import com.withins.core.user.dto.UserAuthToken;
import com.withins.core.user.entity.Role;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final JwtApiVersion version;

    public ResponseCookie generateToken(JwtTokenType tokenType, TokenId tokenId , long userId,
                                        List<Role> authorities) {
        String token = createToken(tokenType, tokenId, userId, authorities);
        String path = getPath(tokenType);
        return createCookie(tokenType.getCookieName(), token, tokenType.getExpiration(), path);
    }

    public ResponseCookie removeToken(JwtTokenType tokenType) {
        String path = getPath(tokenType);
        return createCookie(tokenType.getCookieName(), null, 0, path);
    }

    private String getPath(JwtTokenType tokenType) {
        return tokenType == JwtTokenType.REFRESH_TOKEN
            ? version.generateAPIPath(tokenType.getPath())
            : tokenType.getPath();
    }


    private ResponseCookie createCookie(String cookieName, @Nullable String token, int expiration, String path) {
        return ResponseCookie.from(cookieName, token)
            .path(path)
            .sameSite(SameSite.STRICT.attributeValue())
            .httpOnly(true)
            .secure(true)
            .maxAge(expiration)
            .build();
    }
    // 토큰 생성
    private String createToken(JwtTokenType tokenType, TokenId tokenId,
                               Long userId,
                               List<Role> authorities) {
        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
            .header()
            .keyId(UUID.randomUUID().toString())
            .add("typ", "JWT")
            .and()
            .issuer("WITHINS_JWT")
            .claim("iss", tokenId.provider())
            .claim("sub", tokenId.providerId())
            .claim("userId", userId)
            .claim("scope", authorities.stream()
                .map(Enum::name)
                .toList())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + tokenType.getExpiration() * 1000L))
            .signWith(secretKey)
            .compact();
    }

    public void refresh(UserAuthToken userAuthToken, HttpServletResponse response) {
        TokenId tokenId = new TokenId(userAuthToken.provider(), userAuthToken.providerId());

        // JWT 토큰 생성
        var accessToken = generateToken(JwtTokenType.ACCESS_TOKEN, tokenId, userAuthToken.userId(), userAuthToken.roles());
        var refreshToken = generateToken(JwtTokenType.REFRESH_TOKEN, tokenId, userAuthToken.userId(), userAuthToken.roles());

        // JSON 응답 생성
        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
    }
}
