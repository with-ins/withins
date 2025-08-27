package com.withins.mail.repository;

import com.withins.mail.dto.MailSendDto;


public interface MailSendRepository {

    void sendTextEmail(MailSendDto mailSendDto);
    void sendHtmlEmail(MailSendDto mailSendDto);

}
