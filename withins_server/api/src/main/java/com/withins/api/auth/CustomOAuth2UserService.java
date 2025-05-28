package com.withins.api.auth;

import com.withins.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserService userService;
    private final OidcUserService oidcUserService = new OidcUserService();

    @Override
    public OidcUser loadUser(final OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final String provider = userRequest.getClientRegistration().getRegistrationId();
        log.info("OIDC 인증 시도 - 제공자: {}", provider);

        try {
            final OidcUser oidcUser = oidcUserService.loadUser(userRequest);
            final OidcUserInfo userInfo = extractOidcUserInfo(oidcUser);
            final Long userId = saveOrGetUser(userInfo);
            final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userService.getRole(userId).getKey());

            log.info("OIDC 인증 성공 - userId: {}, 제공자: {}", userId, provider);

            return new DefaultOidcUser(
                    Collections.singleton(authority),
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo()
            );
        } catch (OAuth2AuthenticationException e) {
            log.error("OIDC 인증 실패 - 제공자: {}, 오류: {}", provider, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("예상치 못한 OIDC 인증 오류 - 제공자: {}", provider, e);
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("server_error", "인증 처리 중 오류 발생", null), e);
        }
    }

    private Long saveOrGetUser(final OidcUserInfo userInfo) {
        return userService.saveOrGet(
                userInfo.getSocialUserid(),
                userInfo.getNickname()
        );
    }

    private static OidcUserInfo extractOidcUserInfo(final OidcUser oidcUser) {
        final Map<String, Object> idTokenClaims = oidcUser.getAttributes();
        return OidcUserInfo.from(idTokenClaims);
    }
}
