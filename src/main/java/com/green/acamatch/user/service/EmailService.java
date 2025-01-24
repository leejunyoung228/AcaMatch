package com.green.acamatch.user.service;

import com.green.acamatch.config.constant.EmailConst;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.EmailErrorCode;
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
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final EmailConst emailConst;

    public void sendCodeToEmail(String to, String subject, String template) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(emailConst.getFromEmail(), emailConst.getAlias());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(template, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new CustomException(EmailErrorCode.EMAIL_SEND_FAIL);
        }
    }

    public String getHtmlTemplate(String templateName, Context context) {
        return templateEngine.process(templateName, context);
    }

    public Context getContext(String url, String key, String value) {
        Map<String, Object> dto = new HashMap<>(2);
        dto.put("tokenLink", String.format("%s/%s?%s=%s", emailConst.getBaseUrl(), url, key,value));
        dto.put("maxDate", getMaxDate());
        Context context = new Context();
        context.setVariables(dto);
        return context;
    }
    public Context getContext(String url, Long userId, String pw) {
        Map<String, Object> dto = new HashMap<>(3);
        dto.put("value", pw);
        dto.put("tokenLink", String.format("%s/%s/%s", emailConst.getBaseUrl(), url, userId));
        dto.put("maxDate", getMaxDate());
        Context context = new Context();
        context.setVariables(dto);
        return context;
    }

    private String getMaxDate() {
        Date now = new Date();
        return new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss").format(new Date(now.getTime() + emailConst.getExpiredTime()));
    }

}