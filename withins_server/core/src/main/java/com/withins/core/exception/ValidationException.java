package com.withins.core.exception;

import java.util.Collections;
import java.util.Map;

/**
 * 유효성 검증에 실패했을 때 발생하는 예외입니다.
 */
public class ValidationException extends WithInsException {

    private final Map<String, String> errors;

    /**
     * 일반적인 메시지를 사용하여 새로운 ValidationException을 생성합니다.
     */
    public ValidationException() {
        super("유효성 검증에 실패했습니다");
        this.errors = Collections.emptyMap();
    }

    /**
     * 지정된 상세 메시지를 사용하여 새로운 ValidationException을 생성합니다.
     *
     * @param message 상세 메시지
     */
    public ValidationException(String message) {
        super(message);
        this.errors = Collections.emptyMap();
    }

    /**
     * 상세 메시지와 필드별 오류 정보를 사용하여 새로운 ValidationException을 생성합니다.
     *
     * @param message 상세 메시지
     * @param errors  필드 이름을 key로, 오류 메시지를 value으로 가지는 Map
     */
    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors != null ? errors : Collections.emptyMap();
    }

    /**
     * 필드별로 저장된 검증 오류 정보를 반환합니다.
     *
     * @return 필드 이름을 key로, 오류 메시지를 value으로 가지는 Map
     */
    public Map<String, String> getErrors() {
        return errors;
    }
}
