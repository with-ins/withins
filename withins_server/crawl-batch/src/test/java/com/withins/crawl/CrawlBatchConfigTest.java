package com.withins.crawl;

import com.withins.core.welfarecenter.entity.WelfareCenter;
import com.withins.core.welfarecenter.repository.WelfareCenterRepository;
import com.withins.crawl.config.BatchTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CrawlBatchConfigTest extends BatchTest {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("crawlingJob")
    private Job crawlingJob;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private WelfareCenterRepository welfareCenterRepository;

    private JobLauncherTestUtils jobLauncherTestUtils;

    @BeforeEach
    public void setup() {
        // Set up test data - create a test welfare center
        if (welfareCenterRepository.count() == 0) {
            WelfareCenter testCenter = WelfareCenter.builder()
                    .name("오정노인복지관")
                    .region("인천")
                    .build();
            welfareCenterRepository.save(testCenter);
        }

        // Set up JobLauncherTestUtils
        jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJob(crawlingJob);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJobRepository(jobRepository);
    }

    @Test
    public void testCrawlingJob() throws Exception {
        // Create job parameters
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("syncDate", "2025-06-06")
                .toJobParameters();

        // Run the job
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // Verify job execution status
        assertThat(jobExecution.getStatus().isUnsuccessful()).isFalse();

        // Verify that the job execution context contains the expected keys
        assertThat(jobExecution.getExecutionContext().containsKey("successfulS3Paths")).isTrue();
        System.out.println("successfulS3Paths: " + jobExecution.getExecutionContext().get("successfulS3Paths", List.class).get(0));
        assertThat(jobExecution.getExecutionContext().containsKey("failedCenters")).isTrue();

        // You can add more assertions based on your specific requirements
    }

}