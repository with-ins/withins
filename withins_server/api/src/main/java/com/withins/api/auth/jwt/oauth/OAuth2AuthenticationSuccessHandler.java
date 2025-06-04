package com.withins.api.auth.jwt.oauth;

import com.withins.api.auth.auth2.PrincipalDetails;
import com.withins.api.auth.jwt.JwtTokenProvider;
import com.withins.api.auth.jwt.TokenId;
import com.withins.core.user.entity.SocialUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.List;

import static com.withins.api.auth.jwt.JwtTokenType.*;
import static org.springframework.http.HttpHeaders.*;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        SocialUser user = (SocialUser) principalDetails.getUser();
        TokenId tokenId = new TokenId(user.getProvider().name(), user.getUsername());

        var accessToken = tokenProvider.generateToken(ACCESS_TOKEN, tokenId, user.getId(), List.of(user.getRole()));
        var refreshToken = tokenProvider.generateToken(REFRESH_TOKEN, tokenId, user.getId(), List.of(user.getRole()));

        response.addHeader(SET_COOKIE, accessToken.toString());
        response.addHeader(SET_COOKIE, refreshToken.toString());

        // OAuth2 로그인 시 세션을 초기화한다.
        removeJsessionId(request, response);

        response.sendRedirect("/");
    }

    private static void removeJsessionId(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);
    }
}
