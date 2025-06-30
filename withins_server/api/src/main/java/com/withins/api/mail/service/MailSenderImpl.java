package com.withins.api.mail.service;

import com.withins.api.mail.dto.MailSendDto;
import com.withins.api.mail.repository.MailSendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.withins.api.mail.enums.EmailTemplateType.*;

@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    private final MailSendRepository mailSendRepository;

    @Override
    public void sendAuth(final String toAddress, final int authNumber) {

        String htmlContent = AUTH.toHTMLString(Map.of("authKey", authNumber));

        MailSendDto mail = MailSendDto
            .to(toAddress)
            .write("[WITHINS] 이메일 인증메일입니다.", htmlContent);

        sendHtml(mail);
    }

    @Override
    public void sendText(MailSendDto mailSend) {
        mailSendRepository.sendTextEmail(mailSend);
    }

    @Override
    public void sendHtml(MailSendDto mailSend) {
        mailSendRepository.sendHtmlEmail(mailSend);
    }
}