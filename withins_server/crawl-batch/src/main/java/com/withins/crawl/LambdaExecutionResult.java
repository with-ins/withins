package com.withins.crawl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LambdaExecutionResult {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    private final int statusCode;
    private final String rawResponse;
    private final LambdaResponseData responseData;
    private String errorMessage;

    public static LambdaExecutionResult from(InvokeResponse response) {
        int statusCode = response.statusCode();
        String rawResponse = response.payload().asUtf8String();

        try {
            LambdaResponseData responseData = objectMapper.readValue(rawResponse, LambdaResponseData.class);
            return new LambdaExecutionResult(statusCode, rawResponse, responseData, null);
        } catch (IOException e) {
            return ofError(statusCode, e.getMessage());
        }
    }

    public static LambdaExecutionResult ofError(int statusCode, String errorMessage) {
        return new LambdaExecutionResult(
                statusCode,
                null,
                null,
                errorMessage
        );
    }

    public boolean isSuccess() {
        return statusCode == 200 && responseData != null && responseData.isSuccess();
    }

    public boolean isInvocationSuccessful() {
        return statusCode == 200;
    }

    public String getS3Location() {
        return responseData != null && responseData.getData() != null ? 
               responseData.getData().getS3Location() : null;
    }

    public String getJsonResponse() {
        try {
            return responseData != null ? objectMapper.writeValueAsString(responseData) : null;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Lambda 응답 데이터를 표현하는 내부 클래스
     */
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LambdaResponseData {
        private final boolean success;
        private final String message;
        private final String targetDate;
        private final String jobName;
        private DataObject data; // non-final 유지
        private ErrorObject error; // non-final 유지
        private final String timestamp;

        @JsonCreator
        public LambdaResponseData(
                @JsonProperty("success") boolean success,
                @JsonProperty("message") String message,
                @JsonProperty("targetDate") String targetDate,
                @JsonProperty("jobName") String jobName,
                @JsonProperty("data") DataObject data,
                @JsonProperty("error") ErrorObject error,
                @JsonProperty("timestamp") String timestamp
        ) {
            this.success = success;
            this.message = message;
            this.targetDate = targetDate;
            this.jobName = jobName;
            this.data = data;
            this.error = error;
            this.timestamp = timestamp;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class DataObject {
            private final List<String> processedJobs;
            private final String s3Location;
            private final Integer itemCount;
            private final Integer duration;

            @JsonCreator
            public DataObject(
                    @JsonProperty("processedJobs") List<String> processedJobs,
                    @JsonProperty("s3Location") String s3Location,
                    @JsonProperty("itemCo  unt") Integer itemCount,
                    @JsonProperty("duration") Integer duration
            ) {
                this.processedJobs = processedJobs;
                this.s3Location = s3Location;
                this.itemCount = itemCount;
                this.duration = duration;
            }
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ErrorObject {
            private final String message;
            private final String context;
            private final String stack;

            @JsonCreator
            public ErrorObject(
                    @JsonProperty("message") String message,
                    @JsonProperty("context") String context,
                    @JsonProperty("stack") String stack
            ) {
                this.message = message;
                this.context = context;
                this.stack = stack;
            }
        }
    }
}
