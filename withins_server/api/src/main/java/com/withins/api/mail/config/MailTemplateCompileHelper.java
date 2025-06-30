package com.withins.api.mail.config;

import com.withins.api.mail.enums.EmailTemplateType;
import com.withins.api.mail.exceptions.MailTemplateNotLoadedException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class MailTemplateCompileHelper {

    /**
     * 이메일에 전송될 HTML 양식에 대한 검증로직
     * 1. Spring Container 가 로드되면, 한 번 실행된다.
     * 2. EmailTemplateType 에 정의된 템플릿을 불러와 올바르게 불러올 수 있는지 검증한다.
     *
     * @see EmailTemplateType 이메일에 사용되는 HTML이 정의된 Enum
     * @exception MailTemplateNotLoadedException FileNotFoundException 이 발생하면 Resource Path 를 담아 예외 발생시킨다.
     */
    @PostConstruct
    public void mailTemplateValid() {
        log.info("Validating Mail Template");

        List<String> notFountTemplateList = new ArrayList<>();
        for (EmailTemplateType value : EmailTemplateType.values()) {
            try {
                value.toHTMLString();
            } catch (MailTemplateNotLoadedException e) {
                notFountTemplateList.add(value.getResourcePath());
            }
        }
        if (!notFountTemplateList.isEmpty()) {
            log.error("[{}] Mail Template File Not Found : {}", EmailTemplateType.class.getName() , notFountTemplateList);
            throw new MailTemplateNotLoadedException(notFountTemplateList);
        }
        log.info("Mail Template Valid");
    }
}
