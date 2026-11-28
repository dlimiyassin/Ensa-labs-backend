package com.ensa.labs.zBase.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * SMTP client, created only when MAIL_HOST is set. Port 465 uses implicit TLS (SMTPS),
 * any other port uses STARTTLS (587 for Gmail / Google Workspace).
 */
@Configuration
public class MailConfiguration {

    @Bean
    @ConditionalOnExpression("!'${app.mail.host:}'.isBlank()")
    public JavaMailSender javaMailSender(@Value("${app.mail.host}") String host,
                                         @Value("${app.mail.port:587}") int port,
                                         @Value("${app.mail.username:}") String username,
                                         @Value("${app.mail.password:}") String password,
                                         @Value("${app.mail.starttls:true}") boolean starttls) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host.trim());
        sender.setPort(port);
        sender.setDefaultEncoding("UTF-8");
        if (!username.isBlank()) {
            sender.setUsername(username.trim());
            // App passwords are shown by Google in groups of 4 letters: spaces are not part of it.
            sender.setPassword(password.replace(" ", ""));
        }

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", String.valueOf(!username.isBlank()));
        if (port == 465) {
            sender.setProtocol("smtps");
            props.put("mail.smtps.auth", String.valueOf(!username.isBlank()));
        } else if (starttls) {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
        }
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        return sender;
    }
}
