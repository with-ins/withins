package com.withins.crawl.config;

import com.withins.core.testfixtures.TestContainer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        classes = BatchTest.BatchTestConfiguration.class
)
@ActiveProfiles("crawlbatchtest")
public abstract class BatchTest extends TestContainer {
//    @Autowired
//    private DatabaseCleanup databaseCleanup;
//
//    @AfterEach
//    public void cleanup() {
//        databaseCleanup.execute();
//    }

    @EnableAutoConfiguration
    @ComponentScan(basePackages = {
            "com.withins.core",
            "com.withins.crawl"
    })
    static class BatchTestConfiguration {
    }
}
