package com.withins.api.mail.enums;

import com.withins.api.mail.exceptions.MailTemplateNotLoadedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum EmailTemplateType {

    AUTH("email-template/emailAuth.html"),
    ;

    private final String resourcePath;

    public String toHTMLString() {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new MailTemplateNotLoadedException(resourcePath);
        }
    }
    public String toHTMLString(Map<String, Object> variables) {
        return setVariable(toHTMLString(), variables);
    }

    private String setVariable(String template, Map<String, Object> variables) {
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            template = template.replace(placeholder, String.valueOf(entry.getValue()));
        }
        return template;
    }

}
