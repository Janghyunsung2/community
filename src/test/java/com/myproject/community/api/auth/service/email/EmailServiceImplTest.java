package com.myproject.community.api.auth.service.email;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "test@gmail.com");
    }

    @Test
    @DisplayName("인증코드 전송")
    void sendVerificationEmail() throws MessagingException {
        String testEmail = "test@example.com";

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        emailService.sendVerificationEmail(testEmail);

        verify(javaMailSender, times(1)).createMimeMessage();
    }

    @Test
    @DisplayName("인증코드 확인성공")
    void verifiyCode() throws MessagingException {
        String testEmail = "test@example.com";
        String code = "123456";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().get(testEmail)).thenReturn(code);
        boolean isVerify = emailService.verifyCode(testEmail, code);
        Assertions.assertTrue(isVerify);

    }

    @Test
    @DisplayName("인증코드 실패")
    void verifyCodeFail() throws MessagingException {
        String testEmail = "test@example.com";
        String code = "123456";
        String code2 = "789654";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().get(testEmail)).thenReturn(code2);
        boolean isVerify = emailService.verifyCode(testEmail, code);
        Assertions.assertFalse(isVerify);
    }
}