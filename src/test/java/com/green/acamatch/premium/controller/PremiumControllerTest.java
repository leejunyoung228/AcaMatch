package com.green.acamatch.premium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Controller.PremiumController;
import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.premium.model.PremiumUpdateReq;
import com.green.acamatch.config.GlobalOauth2;
import com.green.acamatch.config.model.ResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PremiumController.class,
        excludeAutoConfiguration = { SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class }
)
@Import(GlobalOauth2.class)
@ExtendWith(MockitoExtension.class) // 추가
class PremiumControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Mock // `@MockBean` 대신 사용
    PremiumService premiumService;

    @Mock
    AcademyMessage academyMessage;

    final String BASE_URL = "/api/academy/premium";

    @Test
    @DisplayName("프리미엄 승인")
    void putPremiumCheck() throws Exception {
        PremiumUpdateReq givenParam = new PremiumUpdateReq();
        givenParam.setAcaId(29L);
        givenParam.setPreCheck(1);

        when(premiumService.updPremium(givenParam)).thenReturn(1); // `given` 대신 `when` 사용

        String paramJson = objectMapper.writeValueAsString(givenParam);

        ResultActions resultActions = mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(paramJson));

        ResultResponse<Integer> res = ResultResponse.<Integer>builder()
                .resultMessage("프리미엄 승인 완료")
                .resultData(1)
                .build();
        String expectedResJson = objectMapper.writeValueAsString(res);

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(premiumService).updPremium(givenParam);
    }
}