package com.withins.core.exception;

/**
 * 엔티티를 찾을 수 없을 때 발생하는 예외입니다.
 */
public class EntityNotFoundException extends WithInsException {

    /**
     * 상세 메시지가 없는 새로운 EntityNotFoundException을 생성합니다.
     */
    public EntityNotFoundException() {
        super("엔티티를 찾을 수 없습니다");
    }

    /**
     * 지정된 상세 메시지를 사용하여 새로운 EntityNotFoundException을 생성합니다.
     *
     * @param message 상세 메시지
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * 특정 엔티티 타입과 ID에 대한 EntityNotFoundException을 생성합니다.
     *
     * @param entityName 엔티티 이름
     * @param id         엔티티 ID
     */
    public EntityNotFoundException(String entityName, Object id) {
        super(String.format("%s with id %s 엔티티를 찾을 수 없습니다", entityName, id));
    }
}