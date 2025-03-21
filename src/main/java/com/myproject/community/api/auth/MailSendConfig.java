package com.myproject.community.api.auth;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailSendConfig {

    @Value("${MALE_HOST}")
    private String host;
    @Value("${MALE_PORT}")
    private int port;
    @Value("${MALE_USERNAME}")
    private String username;
    @Value("${MALE_PASSWORD}")
    private String password;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setProtocol("smtp");
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        ;
        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return javaMailSender;
    }
}
