package com.myproject.community.api.auth.service.email;

import jakarta.mail.MessagingException;

public interface EmailService {
    boolean verifyCode(String email, String inputCode);

    void sendVerificationEmail(String email) throws MessagingException;
}
