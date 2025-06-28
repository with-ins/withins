package com.withins.core.user.service;

import com.withins.core.signup.RequestSignup;
import com.withins.core.signup.ResponseSignup;
import com.withins.core.user.component.UserReader;
import com.withins.core.user.component.UserWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public ResponseSignup signup(RequestSignup requestSignup) {
        Map<String, String> errors = validate(requestSignup);
        if (!errors.isEmpty()) {
            return ResponseSignup.error(errors);
        }
        return null;
    }

    private Map<String, String> validate(RequestSignup requestSignup) {
        Map<String, String> errors = new HashMap<>();
        boolean isUsernameExists = userReader.readByUsername(requestSignup.getUsername()).isPresent();
        if (isUsernameExists) {
            errors.put("username", "이미 사용중인 아이디입니다.");
        }
        boolean isEmailExists = userReader.readByEmail(requestSignup.getEmail()).isPresent();
        if (isEmailExists) {
            errors.put("email", "이미 등록된 이메일입니다.");
        }
        return errors;
    }
}
