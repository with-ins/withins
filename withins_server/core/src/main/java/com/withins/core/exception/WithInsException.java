package com.withins.core.exception;

/**
 * WithIns 애플리케이션의 모든 커스텀 예외를 위한 베이스 예외 클래스입니다.
 * 런타임 예외로, 특정 예외 타입이 이 클래스를 상속받아 확장할 수 있습니다.
 */
public class WithInsException extends RuntimeException {

    /**
     * 상세 메시지가 없는 새로운 WithInsException을 생성합니다.
     */
    public WithInsException() {
        super();
    }

    /**
     * 지정된 상세 메시지를 사용하여 새로운 WithInsException을 생성합니다.
     *
     * @param message 상세 메시지
     */
    public WithInsException(String message) {
        super(message);
    }

    /**
     * 지정된 상세 메시지와 원인을 사용하여 새로운 WithInsException을 생성합니다.
     *
     * @param message 상세 메시지
     * @param cause   예외 원인
     */
    public WithInsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 지정된 원인을 사용하여 새로운 WithInsException을 생성합니다.
     *
     * @param cause 예외 원인
     */
    public WithInsException(Throwable cause) {
        super(cause);
    }
}
