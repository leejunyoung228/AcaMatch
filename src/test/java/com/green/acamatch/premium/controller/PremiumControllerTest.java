package com.green.acamatch.premium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Controller.PremiumController;
import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.premium.model.PremiumUpdateReq;
import com.green.acamatch.config.GlobalOauth2;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.model.ResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PremiumController.class
        , excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class PremiumControllerTest {

    @Autowired
     MockMvc mockMvc;

    @MockitoBean
     PremiumService premiumService;

    @MockitoBean
    AcademyMessage academyMessage;

    @MockitoBean
    JwtConst jwtConst;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("프리미엄 승인 - 성공")
    void putPremiumCheck_Success() throws Exception {
        // given
        PremiumUpdateReq request = new PremiumUpdateReq();
        request.setAcaId(1L);
        request.setPreCheck(1);

        given(premiumService.updPremium(any())).willReturn(1);
        given(academyMessage.getMessage()).willReturn("프리미엄 학원이 승인되었습니다.");

        // when & then
        mockMvc.perform(put("/academy/premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("프리미엄 학원이 승인되었습니다."))
                .andExpect(jsonPath("$.resultData").value(1));
    }
}