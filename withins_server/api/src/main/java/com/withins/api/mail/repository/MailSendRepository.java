package com.withins.api.mail.repository;

import com.withins.api.mail.dto.MailSendDto;


public interface MailSendRepository {

    void sendTextEmail(MailSendDto mailSendDto);
    void sendHtmlEmail(MailSendDto mailSendDto);

}
