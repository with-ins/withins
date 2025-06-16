package com.withins.crawl;

import com.withins.core.welfarecenter.entity.WelfareCenter;
import com.withins.core.welfarecenter.repository.WelfareCenterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LambdaCrawlingTasklet implements Tasklet {
    private final LambdaClient lambdaClient;
    private final String functionName;
    private final WelfareCenterRepository welfareCenterRepository;
    private final LocalDate targetDate;

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
        // 크롤링 대상 조회
        List<WelfareCenter> centers = welfareCenterRepository.findAll();

        // 성공한 크롤링 결과의 S3 경로를 저장할 리스트
        List<String> successfulS3Paths = new ArrayList<>();

        // 실패한 크롤링 대상 정보를 저장할 리스트
        List<String> failedCenters = new ArrayList<>();

        for (WelfareCenter center : centers) {
            try {
                // Lambda 함수 호출 페이로드 생성
                String payload = createPayload(targetDate, center.getName());

                // Lambda 함수 호출
                InvokeRequest request = InvokeRequest.builder()
                    .functionName(functionName)
                    .payload(SdkBytes.fromUtf8String(payload))
                    .build();

                InvokeResponse result = lambdaClient.invoke(request);

                // 응답 처리
                if (result.statusCode() == 200) {
                    // 응답 JSON 파싱
                    String response = new String(result.payload().asByteArray(), StandardCharsets.UTF_8);
                    // 직접 응답 JSON 파싱 (중첩된 body 필드 없음)
                    JSONObject jsonResponse = new JSONObject(response);

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        // 성공한 경우 S3 경로 저장
                        JSONObject dataJson = jsonResponse.getJSONObject("data");

                        String s3Location = dataJson.getString("s3Location");

                        successfulS3Paths.add(s3Location);
                        log.info("Successfully crawled center: {}, S3 path: {}", center.getName(), s3Location);
                    } else {
                        // 실패한 경우 실패 목록에 추가
                        failedCenters.add(center.getName());
                        log.warn("Failed to crawl center: {}", center.getName());
                    }
                } else {
                    //TODO Lambda 호출 자체가 실패한 경우, 재시도 로직이 필요할까?
                    // Lambda 호출 자체가 실패한 경우
                    failedCenters.add(center.getName());
                    log.error("Lambda invocation failed for center: {}, status code: {}", 
                            center.getName(), result.statusCode());
                }
            } catch (Exception e) {
                // 예외 발생 시 실패 목록에 추가
                failedCenters.add(center.getName());
                log.error("Exception while processing center: {}", center.getName(), e);
            }
        }

        // 성공한 S3 경로와 실패한 센터 정보를 Job ExecutionContext에 저장
        // 다음 Step에서 이 정보를 활용할 수 있도록 함
        ExecutionContext executionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
        executionContext.put("successfulS3Paths", successfulS3Paths);
        executionContext.put("failedCenters", failedCenters);

        log.info("Crawling completed. Success: {}, Failed: {}", 
                successfulS3Paths.size(), failedCenters.size());

        return RepeatStatus.FINISHED;
    }

    private String createPayload(LocalDate targetDate, String jobName) {
        // 크롤링 대상 정보를 Lambda 함수에 전달할 JSON 생성
        JSONObject payload = new JSONObject();
        payload.put("targetDate", targetDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .put("jobName", jobName);

        return payload.toString();
    }
}
