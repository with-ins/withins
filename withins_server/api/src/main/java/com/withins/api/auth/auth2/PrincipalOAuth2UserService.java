package com.withins.api.auth.auth2;

import com.withins.api.auth.auth2.converter.DefaultOAuth2User;
import com.withins.api.auth.auth2.converter.OAuth2Converter;
import com.withins.core.user.entity.Provider;
import com.withins.core.user.entity.Role;
import com.withins.core.user.entity.SocialUser;
import com.withins.core.user.entity.User;
import com.withins.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Provider provider = Provider.findByProvider(userRequest.getClientRegistration().getClientName());
        DefaultOAuth2User userInfo = OAuth2Converter.of(provider).convert(oAuth2User);
        String username = String.format("%s_%s", provider, userInfo.getProviderId());

        User user = userService.saveOrGet(username,
                () -> SocialUser.builder()
                    .username(username)
                    .nickname(userInfo.getName())
                    .role(Role.USER)
                    .provider(userInfo.getProvider())
                    .providerId(userInfo.getProviderId())
                    .build()
            );
        return new PrincipalDetails(user);
    }
}
