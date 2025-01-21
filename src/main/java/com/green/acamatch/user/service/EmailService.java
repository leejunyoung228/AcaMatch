package com.green.acamatch.user.service;

import com.green.acamatch.config.constant.EmailConst;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.EmailErrorCode;
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
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final EmailConst emailConst;

    public void sendCodeToEmail(String to, String token) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setSubject(emailConst.getSubject());
            helper.setText(getHtmlTemplate(token), true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException(EmailErrorCode.EMAIL_SEND_FAIL);
        }
    }

    private String getHtmlTemplate(String token) {
        return templateEngine.process( emailConst.getTemplateName(), getContext(token));
    }

    private Context getContext(String token) {
        Map<String, Object> dto = new HashMap<>(2);
        dto.put("tokenLink", String.format("%s/%s?%s=%s", emailConst.getBaseUrl(), emailConst.getUrl(), emailConst.getKey(),token));
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