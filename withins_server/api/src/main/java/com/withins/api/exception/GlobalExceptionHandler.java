package com.withins.api.exception;

import com.withins.core.exception.EntityNotFoundException;
import com.withins.core.exception.ValidationException;
import com.withins.core.exception.WithInsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * API 모듈 전역 예외 핸들러입니다.
 * 애플리케이션에서 발생하는 모든 예외를 처리하여 적절한 HTTP 응답으로 변환합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * EntityNotFoundException을 처리합니다.
     *
     * @param ex 발생한 예외
     * @return 에러 응답을 담은 ResponseEntity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * ValidationException을 처리합니다.
     *
     * @param ex 발생한 예외
     * @return 필드별 에러 정보를 포함한 ResponseEntity
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(ValidationException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                ex.getErrors()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * WithInsException의 하위 예외들을 처리합니다.
     *
     * @param ex 발생한 예외
     * @return 에러 응답을 담은 ResponseEntity
     */
    @ExceptionHandler(WithInsException.class)
    public ResponseEntity<ErrorResponse> handleWithInsException(WithInsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 그 외 모든 예외를 처리합니다.
     *
     * @param ex 발생한 예외
     * @return 에러 응답을 담은 ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "예기치 못한 오류가 발생했습니다",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * API 에러 응답 DTO
     *
     * @param status    HTTP 상태 코드
     * @param message   에러 메시지
     * @param timestamp 발생 시각
     */
    public record ErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp
    ) {
    }

    /**
     * 검증 에러 응답 DTO
     *
     * @param status    HTTP 상태 코드
     * @param message   에러 메시지
     * @param timestamp 발생 시각
     * @param errors    필드별 오류 메시지 맵
     */
    public record ValidationErrorResponse(
            int status,
            String message,
            LocalDateTime timestamp,
            Map<String, String> errors
    ) {
    }
}
