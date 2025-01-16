package com.green.studybridge.user.auth;

import com.green.studybridge.config.ServerConst;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
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
