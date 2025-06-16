package com.withins.crawl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CrawlJobScheduler {

    private final JobLauncher jobLauncher;
    
    @Qualifier("crawlingJob")
    private final Job crawlingJob;

    /**
     * 매일 오전 9시에 크롤링 작업 실행
     * cron = "초 분 시 일 월 요일"
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void runDailyCrawlingJob() {
        log.info("Starting scheduled crawling job");
        
        // 현재 날짜를 문자열로 변환
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        
        // 고유한 JobParameters 생성 (매번 다른 타임스탬프 사용)
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("targetDate", today)
                .toJobParameters();
        
        try {
            jobLauncher.run(crawlingJob, jobParameters);
            log.info("Scheduled crawling job completed successfully");
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("Error executing scheduled crawling job", e);
        }
    }
    
    /**
     * 테스트용: 매 시간마다 크롤링 작업 실행 (개발 환경에서만 활성화)
     */
    @Scheduled(fixedRate = 1800000) // 30분마다 실행 (첫 실행은 애플리케이션 시작 직후)
    public void runHourlyCrawlingJobForTesting() {
        log.info("Starting hourly test crawling job");
        
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("targetDate", LocalDate.now().format(DateTimeFormatter.ISO_DATE))
                .addLong("timestamp", System.currentTimeMillis())  // 타임스탬프 추가
                .toJobParameters();
        
        try {
            jobLauncher.run(crawlingJob, jobParameters);
            log.info("Hourly test crawling job completed successfully");
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.error("Error executing hourly test crawling job", e);
        }
    }
}