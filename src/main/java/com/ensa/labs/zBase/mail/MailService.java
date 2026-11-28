package com.ensa.labs.zBase.mail;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/** Sends the application's e-mails. Does nothing (and says so in the log) when MAIL_HOST is not set. */
@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private static final String SENDER_NAME = "ENSA Labs";

    private final ObjectProvider<JavaMailSender> mailSender;
    private final String from;

    public MailService(ObjectProvider<JavaMailSender> mailSender,
                       @Value("${app.mail.from:}") String from,
                       @Value("${app.mail.username:}") String username) {
        this.mailSender = mailSender;
        this.from = from.isBlank() ? username.trim() : from.trim();
    }

    public boolean isEnabled() {
        return mailSender.getIfAvailable() != null && !from.isBlank();
    }

    /** Sends a message with a plain-text and an HTML version. Returns false when it could not be sent. */
    public boolean send(String to, String subject, String text, String html) {
        JavaMailSender sender = mailSender.getIfAvailable();
        if (sender == null || from.isBlank()) {
            log.warn("E-mail not sent: MAIL_HOST / MAIL_FROM are not configured");
            return false;
        }
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress(from, SENDER_NAME, "UTF-8"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, html);
            sender.send(message);
            return true;
        } catch (Exception e) {
            // The recipient is not logged: only the failure, to keep personal data out of the logs.
            log.error("E-mail could not be sent: {}", e.getMessage());
            return false;
        }
    }
}
