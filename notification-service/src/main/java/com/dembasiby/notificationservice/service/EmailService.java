package com.dembasiby.notificationservice.service;

import com.dembasiby.notificationservice.exception.NotificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Slf4j
@Service
public class EmailService {

    private final SesClient sesClient;

    @Value("${aws.ses.sender-email}")
    private String senderEmail;

    public EmailService(@Autowired(required = false) SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void sendEmail(String recipientEmail, String subject, String body) {
        if (sesClient == null) {
            log.info("SES client not configured. Logging email instead.");
            log.info("To: {}, Subject: {}, Body: {}", recipientEmail, subject, body);
            return;
        }

        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .source(senderEmail)
                    .destination(Destination.builder()
                            .toAddresses(recipientEmail)
                            .build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).charset("UTF-8").build())
                            .body(Body.builder()
                                    .text(Content.builder().data(body).charset("UTF-8").build())
                                    .build())
                            .build())
                    .build();

            sesClient.sendEmail(request);
            log.info("Email sent to {} with subject: {}", recipientEmail, subject);
        } catch (SesException e) {
            log.error("Failed to send email to {}: {}", recipientEmail, e.getMessage());
            throw new NotificationException("Email delivery failed: " + e.getMessage());
        }
    }
}
