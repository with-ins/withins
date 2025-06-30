package com.withins.api.mail.service;

import com.withins.api.mail.dto.MailSendDto;
import org.springframework.stereotype.Service;

@Service
public interface MailSender {

    void sendAuth(String toAddress,int authNumber);

    void sendText(MailSendDto mailSend);
    void sendHtml(MailSendDto mailSend);
}
