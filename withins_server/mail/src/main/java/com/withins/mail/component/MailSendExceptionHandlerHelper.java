package com.withins.mail.component;

import com.withins.mail.exceptions.*;
import jakarta.mail.Address;
import jakarta.mail.SendFailedException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class MailSendExceptionHandlerHelper {

    public void mailSendTemplate(Runnable runnable) {
        try {
            runnable.run();
        } catch (MailSendException ex) {
            for (Exception e : ex.getMessageExceptions()) {
                switch (e) {
                    case SendFailedException se -> sendFailedHandle(se);
                    case MailConnectException mc -> mailConnectionHandle(mc);
                    default -> notThrowException();
                }
            }
            throw new EmailInternalException();
        } catch (MailAuthenticationException e) {
            log.error("SMTP 아이디 또는 비밀번호가 올바르지 않습니다.");
            throw new SMTPBadCredentialsException();
        }
    }

    private void notThrowException() {}

    private void sendFailedHandle(SendFailedException e) {
        switch (e.getCause()) {
            case SMTPAddressFailedException a -> addressFailedHandle(e);
            default -> throw new EmailExternalException("Unexpected value SendFailedException : " + e);
        }
    }

    private void mailConnectionHandle(MailConnectException e) {
        log.error("SMTP 서버 주소 또는 포트가 올바르지 않습니다. 호스트 : {}, 포트 : {}", e.getHost(), e.getPort());
        throw new EmailConnectionException(e.getHost(), e.getPort());
    }

    private void addressFailedHandle(SendFailedException e) {
        log.error("이메일 형식이 올바르지 않습니다. 이메일 : {}", Arrays.toString(e.getInvalidAddresses()));
        throw new InvalidEmailException(Arrays.stream(e.getInvalidAddresses()).map(Address::toString).toList());
    }


}
