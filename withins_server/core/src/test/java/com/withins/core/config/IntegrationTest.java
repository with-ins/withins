package com.withins.core.config;

import com.withins.core.testfixtures.support.DatabaseCleanup;
import com.withins.core.testfixtures.support.SpringBootTestSupport;
import com.withins.core.testfixtures.support.TestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        classes = IntegrationTest.CoreTestConfiguration.class
)
@ActiveProfiles("test")
public abstract class IntegrationTest extends TestContainer {

    @EnableAutoConfiguration
    @ComponentScan(basePackages = {"com.withins.core"})
    static class CoreTestConfiguration {
    }

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void cleanup() {
        databaseCleanup.execute();
    }

    @Autowired
    protected SpringBootTestSupport testSupport;
}
