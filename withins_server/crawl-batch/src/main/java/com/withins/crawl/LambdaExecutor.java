package com.withins.crawl;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.time.LocalDate;

@RequiredArgsConstructor
public class LambdaExecutor {
    private final LambdaClient lambdaClient;

    public LambdaExecutionResult execute(final String functionName, final LocalDate targetDate, final String jobName) {
        String payload = createPayload(targetDate, jobName);

        InvokeRequest request = InvokeRequest.builder()
                .functionName(functionName)
                .payload(SdkBytes.fromUtf8String(payload))
                .build();

        try {
            InvokeResponse response = lambdaClient.invoke(request);
            return LambdaExecutionResult.from(response);
        } catch (Exception e) {
            return error();
        }
    }

    private String createPayload(LocalDate targetDate, String jobName) {
        // 크롤링 대상 정보를 Lambda 함수에 전달할 JSON 생성
        return new JSONObject()
                .put("targetDate", targetDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .put("jobName", jobName)
                .toString();
    }

    public LambdaExecutionResult error() {
        return LambdaExecutionResult.ofError(
                500,
                "AWS Lambda 함수 호출이 실패했습니다"
        );
    }
}
