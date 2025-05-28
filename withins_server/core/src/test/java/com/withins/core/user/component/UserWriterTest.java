package com.withins.core.user.component;

import com.withins.core.config.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UserWriterTest extends IntegrationTest {

    @Autowired private UserWriter sut;

    @Test
    void 유저를_저장한다() {
        //when
        Long result = sut.save("testSocialUserId", "testNickname");
        //then
        assertThat(result).isNotNull();
    }
}
