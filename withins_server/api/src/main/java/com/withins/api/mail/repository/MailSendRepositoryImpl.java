package com.withins.api.mail.repository;

import com.withins.api.mail.component.MailSendExceptionHandlerHelper;
import com.withins.api.mail.dto.MailSendDto;
import com.withins.api.mail.exceptions.EmailInternalException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MailSendRepositoryImpl implements MailSendRepository {

    private final JavaMailSender mailSender;
    private final MailSendExceptionHandlerHelper templateHelper;

    @Override
    public void sendTextEmail(MailSendDto mailSendDto) {
        SimpleMailMessage message = generateTextMessage(mailSendDto);
        templateHelper.mailSendTemplate(() -> mailSender.send(message));
    }

    @Override
    public void sendHtmlEmail(MailSendDto mailSendDto) {
        MimeMessage message = generateHTMLMessage(mailSendDto);
        templateHelper.mailSendTemplate(() -> mailSender.send(message));
    }

    private SimpleMailMessage generateTextMessage(MailSendDto mailDto) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(mailDto.getAddress());                // 받는 사람 이메일
        smm.setSubject(mailDto.getSubject());            // 이메일 제목
        smm.setText(mailDto.getContent());               // 이메일 내용
        return smm;
    }

    private MimeMessage generateHTMLMessage(MailSendDto mailDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(mailDto.getAddress());
            helper.setSubject(mailDto.getSubject());
            helper.setText(mailDto.getContent(), true);
            return message;
        } catch (MessagingException e) {
            throw new EmailInternalException("MimeMessage 생성 실패 : " + e.getMessage());
        }
    }


}
