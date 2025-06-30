package com.withins.api.mail.exceptions;

import com.withins.api.mail.enums.EmailTemplateType;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class MailTemplateNotLoadedException extends EmailInternalException {

    private final List<String> notFountTemplateList;

    public MailTemplateNotLoadedException(List<String> resourceNames) {
        super("[" + EmailTemplateType.class.getName() + "] 이메일 템플릿을 로드하지 못했습니다. 찾을 수 없는 파일 : " + resourceNames);
        this.notFountTemplateList = resourceNames;
    }

    public MailTemplateNotLoadedException(String resourceName) {
        super("[" + EmailTemplateType.class.getName() + "] 이메일 템플릿을 로드하지 못했습니다. 찾을 수 없는 파일 : " + resourceName);
        this.notFountTemplateList = List.of(resourceName);
    }

    public MailTemplateNotLoadedException() {
        super("[" + EmailTemplateType.class.getName() + "] 알 수 없는 이유로 이메일 템플릿을 로드하지 못했습니다.");
        this.notFountTemplateList = Collections.emptyList();
    }
}
