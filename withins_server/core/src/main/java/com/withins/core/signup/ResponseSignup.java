package com.withins.core.signup;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseSignup {

    private final String result;
    private final String message;
    private final Map<String, String> errors;

    public static ResponseSignup error(BindingResult result) {
        return new ResponseSignup(
            "ERROR",
            "검증에 실패했습니다.",
            result.getFieldErrors().stream().collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (existing, replacement) -> replacement
            ))
        );
    }
    public static ResponseSignup error(Map<String, String> result) {
        return new ResponseSignup(
            "ERROR",
            "검증에 실패했습니다.",
            result
        );
    }

    public static ResponseSignup ok() {
        return new ResponseSignup("OK", "검증 성공", Collections.emptyMap());
    }

}
