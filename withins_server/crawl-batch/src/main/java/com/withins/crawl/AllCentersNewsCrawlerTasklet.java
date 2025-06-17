package com.withins.crawl;

import com.withins.core.welfarecenter.component.WelfareCenterReader;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import software.amazon.awssdk.services.lambda.LambdaClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AllCentersNewsCrawlerTasklet implements Tasklet {
    private final LambdaClient lambdaClient;
    private final String functionName;
    private final LocalDate targetDate;
    private final WelfareCenterReader welfareCenterReader;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        List<WelfareCenter> welfareCenters = welfareCenterReader.readAll();

        List<String> successfulS3Paths = new ArrayList<>();
        List<String> failedCenters = new ArrayList<>();

        LambdaExecutor lambdaExecutor = new LambdaExecutor(lambdaClient);

        for (WelfareCenter welfareCenter : welfareCenters) {
            log.debug("NEWS 크롤링 시작 - welfareCenter={}", welfareCenter.getName());
            LambdaExecutionResult result = lambdaExecutor.execute(functionName, targetDate, welfareCenter.getName());

            if (result.isSuccess()) {
                successfulS3Paths.add(result.getS3Location());
                log.debug("NEWS 크롤링 성공 - welfareCenter={}, s3Location={}", welfareCenter.getName(), result.getS3Location() );
            } else if (result.isInvocationSuccessful()) {
                failedCenters.add(welfareCenter.getName());
                log.warn("NEWS 크롤링 실패 - {}", result.getJsonResponse());
            } else {
                log.warn("NEWS 크롤링 Lambda 호출 실패 - {}", welfareCenter.getName());
            }
        }

        storeCrawlingResultsInContext(contribution, successfulS3Paths, failedCenters);
        return RepeatStatus.FINISHED;
    }

    private void storeCrawlingResultsInContext(StepContribution contribution,
                                               List<String> successfulS3Paths,
                                               List<String> failedCenters) {
        // 성공한 S3 경로와 실패한 복지관 정보를 Job ExecutionContext에 저장
        // 다음 Step에서 이 정보를 활용할 수 있도록 함
        ExecutionContext executionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
        executionContext.put("successfulS3Paths", successfulS3Paths);
        executionContext.put("failedCenters", failedCenters);

        log.info("NEWS 크롤링 요약 - 크롤링 대상={}, 크롤링 성공={}, 크롤링 실패={}",
                successfulS3Paths.size() + failedCenters.size(),
                successfulS3Paths.size(),
                failedCenters.size());
    }
}
