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
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CrawlJobScheduler {

    private final JobLauncher jobLauncher;
    
    private final Job allCentersNewsCrawlJob;

    /**
     * 매일 오전 3시에 크롤링 작업 실행
     * cron = "초 분 시 일 월 요일"
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void runDailyCrawlingJob() {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("News 크롤링 스케쥴러 시작 - 현재시간={}", startTime.format(DateTimeFormatter.ISO_DATE_TIME));

        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE);
        
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("targetDate", yesterday)
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        
        try {
            jobLauncher.run(allCentersNewsCrawlJob, jobParameters);
            LocalDateTime endTime = LocalDateTime.now();

            log.info("News 크롤링 스케쥴러 종료 - 시작시간={}, 종료시간={}, 소요시간={}초",
                    startTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    endTime.format(DateTimeFormatter.ISO_DATE_TIME),
                    endTime.minusSeconds(startTime.getSecond()).getSecond());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.warn("News 크롤링 스케쥴러 실패 - {}", e.getMessage());
        }
    }

    // 테스트용
//    @Scheduled(fixedRate = 1800000) // 30분마다 실행 (첫 실행은 애플리케이션 시작 직후)
//    public void runDailyCrawlingJobTest() {
//        LocalDateTime startTime = LocalDateTime.now();
//        log.info("News 크롤링 스케쥴러 시작 - 현재시간={}", startTime.format(DateTimeFormatter.ISO_DATE_TIME));
//
//        String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_DATE);
//
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("targetDate", yesterday)
//                .addLong("timestamp", System.currentTimeMillis())
//                .toJobParameters();
//
//        try {
//            jobLauncher.run(allCentersNewsCrawlJob, jobParameters);
//            LocalDateTime endTime = LocalDateTime.now();
//
//            log.info("News 크롤링 스케쥴러 종료 - 시작시간={}, 종료시간={}, 소요시간={}초",
//                    startTime.format(DateTimeFormatter.ISO_DATE_TIME),
//                    endTime.format(DateTimeFormatter.ISO_DATE_TIME),
//                    endTime.minusSeconds(startTime.getSecond()).getSecond());
//        } catch (JobExecutionAlreadyRunningException | JobRestartException |
//                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
//            log.warn("News 크롤링 스케쥴러 실패 - {}", e.getMessage());
//        }
//    }
}
