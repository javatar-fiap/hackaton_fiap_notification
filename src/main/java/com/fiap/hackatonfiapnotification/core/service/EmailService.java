package com.fiap.hackatonfiapnotification.core.service;

import com.fiap.hackatonfiapnotification.application.exception.EmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {

    private final String smtpEmail;
    private final String smtpPassword;

    public EmailService(@Value("${smtp.email}") String smtpEmail,
                        @Value("${smtp.password}") String smtpPassword) {
        this.smtpEmail = smtpEmail;
        this.smtpPassword = smtpPassword;
    }


    public void sendEmail(String to, String subject, String body, byte[] attachment, String zipKeyS3) {
        var host = "smtp.gmail.com";
        var properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        var session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpEmail, smtpPassword);
            }
        });

        try {
            var message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            var textPart = new MimeBodyPart();
            textPart.setText(body);

            var multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            if (attachment != null) {
                var attachmentPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(attachment, "application/zip");
                attachmentPart.setDataHandler(new DataHandler(source));

                var fileName = zipKeyS3.substring(zipKeyS3.lastIndexOf("/") + 1);
                attachmentPart.setFileName(fileName);

                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new EmailException("Error to send e-mail", e);
        }

    }
}
