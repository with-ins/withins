package com.withins.crawl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LambdaExecutionResult {

    private final int statusCode;
    private final String rawResponse;
    private final JSONObject jsonResponse;
    private final LambdaResponseData responseData;
    private String errorMessage;

    public static LambdaExecutionResult from(InvokeResponse response) {
        int statusCode = response.statusCode();
        String rawResponse = new String(response.payload().asByteArray(), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(rawResponse);
        LambdaResponseData responseData = LambdaResponseData.from(jsonObject);

        return new LambdaExecutionResult(statusCode, rawResponse, jsonObject, responseData);
    }

    public static LambdaExecutionResult ofError(int statusCode, String errorMessage) {
        return new LambdaExecutionResult(
                statusCode,
                null,
                null,
                null,
                errorMessage
        );
    }

    public boolean isSuccess() {
        return statusCode == 200 && responseData.isSuccess();
    }

    public boolean isInvocationSuccessful() {
        return statusCode == 200;
    }

    public String getS3Location() {
        return responseData.getS3Location();
    }

    public String getJsonResponse() {
        return jsonResponse.toString();
    }

    /**
     * Lambda 응답 데이터를 표현하는 내부 클래스
     */
    //TODO Jackson을 사용한 개선 예정
    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LambdaResponseData {
        private final boolean success;
        private final String message;
        private final String targetDate;
        private final String jobName;
        private final String error;
        private final String timestamp;
        private final String s3Location;
        //TODO processedJobs를 List에서 String으로 개선 예정(크롤링 서버 수정 후)
        private final List<String> processedJobs;
        private final Integer itemCount;
        private final Integer duration;

        public static LambdaResponseData from(JSONObject json) {
            boolean success = json.optBoolean("success");
            String message = json.optString("message", null);
            String targetDate = json.optString("targetDate", null);
            String jobName = json.optString("jobName", null);
            String error = json.optString("error", null);
            String timestamp = json.optString("timestamp", null);

            String s3Location = null;
            List<String> processedJobs = null;
            Integer itemCount = null;
            Integer duration = null;

            // 성공한 경우 data 객체에서 추가 정보 추출
            if (success && json.has("data")) {
                JSONObject data = json.getJSONObject("data");
                s3Location = data.optString("s3Location", null);
                itemCount = data.has("itemCount") ? data.getInt("itemCount") : null;
                duration = data.has("duration") ? data.getInt("duration") : null;

                if (data.has("processedJobs")) {
                    processedJobs = IntStream.range(0, data.getJSONArray("processedJobs").length())
                            .mapToObj(i -> data.getJSONArray("processedJobs").getString(i))
                            .collect(Collectors.toList());
                }
            }

            return new LambdaResponseData(
                    success, message, targetDate, jobName, error, timestamp,
                    s3Location, processedJobs, itemCount, duration
            );
        }
    }
}
