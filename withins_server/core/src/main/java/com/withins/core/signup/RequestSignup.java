package com.withins.core.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RequestSignup {

    @NotBlank(message = "사용자명을 입력해주세요.")
    @Size(min = 4, max = 20, message = "4 ~ 20자의 영문,숫자만 사용가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "4 ~ 20자의 영문,숫자만 사용가능합니다.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "8자리 이상 영문 대소문자, 숫자, 특수문자를 조합해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
        message = "8자리 이상 영문 대소문자, 숫자, 특수문자를 조합해주세요.")
    private String password;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

}
