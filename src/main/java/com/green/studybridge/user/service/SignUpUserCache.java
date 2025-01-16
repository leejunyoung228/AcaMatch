package com.green.studybridge.user.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.green.studybridge.config.ServerConst;
import com.green.studybridge.user.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SignUpUserCache {
    private final Cache<String, User> tokenCache;
    private final Cache<String, Boolean> emailCache;

    public SignUpUserCache() {
        this.tokenCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();
        this.emailCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();
    }

    public void saveToken(String token, User user) {
        if (emailCache.getIfPresent(user.getEmail()) != null) {
            throw new RuntimeException("인증 메일이 이미 전송 되었습니다.");
        }
        this.tokenCache.put(token, user);
        this.emailCache.put(user.getEmail(), Boolean.TRUE);
    }

    public User verifyToken(String token) {
        User user = this.tokenCache.getIfPresent(token);
        if (user == null) {
            throw new RuntimeException("유효하지 않은 토큰 입니다");
        }
        tokenCache.invalidate(token);
        emailCache.invalidate(user.getEmail());
        return user;
    }

    @Service
    @RequiredArgsConstructor
    public static class AuthService {
        private final JavaMailSender javaMailSender;
        private final TemplateEngine templateEngine;
        private final ServerConst authConst;

        public void sendCodeToEmail(String to, String subject, String token) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            try {
                helper = new MimeMessageHelper(mimeMessage, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(getHtmlTemplate(token), true);
                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                throw new RuntimeException("이메일 전송 실패");
            }
        }

        private String getHtmlTemplate(String token) {
            return templateEngine.process("emailTemplate", getContext(token));
        }

        private Context getContext(String token) {
            Map<String, Object> dto = new HashMap<>(2);
            dto.put("tokenLink", String.format("%s/api/auth?token=%s", authConst.getServerUrl(),token));
            dto.put("maxDate", getMaxDate());
            Context context = new Context();
            context.setVariables(dto);
            return context;
        }

        private String getMaxDate() {
            Date now = new Date();
            return new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss").format(new Date(now.getTime() + (900_000)));
        }
    }
}
