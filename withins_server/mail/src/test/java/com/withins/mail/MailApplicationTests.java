package com.withins.mail;

import com.withins.mail.enums.EmailTemplate;
import com.withins.mail.exceptions.MailTemplateNotLoadedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class MailApplicationTests {

    /**
     * 이메일에 전송될 HTML 양식에 대한 검증로직
     * - EmailTemplateType 에 정의된 템플릿을 불러와 올바르게 불러올 수 있는지 검증한다.
     *
     * @throws MailTemplateNotLoadedException FileNotFoundException 이 발생하면 Resource Path 를 담아 예외 발생시킨다.
     * @see EmailTemplate 이메일에 사용되는 HTML이 정의된 Enum
     */
    @Test
    @DisplayName("HTML 메일 템플릿 경로가 모두 로드할 수 있어야 한다.")
    void mailTemplateValid() {

        List<String> notFountTemplateList = new ArrayList<>();
        for (EmailTemplate value : EmailTemplate.values()) {
            try {
                value.toHTMLString();
            } catch (MailTemplateNotLoadedException e) {
                notFountTemplateList.add(value.getResourcePath());
            }
        }

        if (!notFountTemplateList.isEmpty()) {
            throw new MailTemplateNotLoadedException(notFountTemplateList);
        }
    }

}
