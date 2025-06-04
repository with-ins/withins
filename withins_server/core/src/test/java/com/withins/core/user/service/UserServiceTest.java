package com.withins.core.user.service;

import com.withins.core.config.IntegrationTest;
import com.withins.core.user.entity.Provider;
import com.withins.core.user.entity.Role;
import com.withins.core.user.entity.SocialUser;
import com.withins.core.user.entity.User;
import com.withins.core.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest extends IntegrationTest {

    @Autowired
    private UserService sut;
    @Autowired
    private UserRepository userRepository;

    @Test
    void 이미_가입된_회원은_로그인시_재가입되지_않는다() {
        //given
        userRepository.save(
                SocialUser.builder()
                        .provider(Provider.KAKAO)
                        .providerId("testSocialUserId")
                        .username("testSocialUserId")
                        .nickname("testNickname")
                        .role(Role.USER)
                        .build()
        );
        //when
        sut.saveOrGet("testSocialUserId", () -> SocialUser.builder()
            .provider(Provider.KAKAO)
            .providerId("testSocialUserId")
            .username("testSocialUserId2")
            .nickname("testNickname2")
            .role(Role.USER)
            .build());
        //then
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);
    }
}
