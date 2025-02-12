package com.myproject.community.api.auth.service.email;

import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    private final RedisTemplate<String, String> redisTemplate;

    @Async
    public void sendVerificationEmail(String email) throws MessagingException {
        String code = generateVerificationCode();
        saveVerificationCode(email, code);


        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");


        mimeMessageHelper.setFrom(fromEmail);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("[온더잇] 이메일 인증 코드");
        mimeMessageHelper.setText("인증 코드: " + code);
        mimeMessageHelper.setSentDate(new Date());

        mailSender.send(mimeMessage);


    }


    public boolean verifyCode(String email, String inputCode){
        String storedCode = redisTemplate.opsForValue().get(email);
        return storedCode != null && storedCode.equals(inputCode);
    }

    private void saveVerificationCode(String email, String code){
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    private String generateVerificationCode(){
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 코드
    }

}
