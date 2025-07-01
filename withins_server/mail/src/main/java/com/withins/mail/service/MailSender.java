package com.withins.mail.service;

import com.withins.mail.dto.MailSendDto;
import org.springframework.stereotype.Service;

@Service
public interface MailSender {

    void sendAuth(String toAddress,int authNumber);

    void sendText(MailSendDto mailSend);
    void sendHtml(MailSendDto mailSend);
}
