package com.green.acamatch.premium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Controller.PremiumController;
import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.premium.model.PremiumUpdateReq;
import com.green.acamatch.config.model.ResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PremiumController.class
        , excludeAutoConfiguration = { SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class } //자동으로 설정하는거 (여기 2개) 제외한다.
        //만약 Oauth2AuthenticationCheckRedirectUriFilter를 빈등록해야 한다면 아래 내용을 추가하면 됨
        // , excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { Oauth2AuthenticationCheckRedirectUriFilter.class}) }
)
class PremiumControllerTest {
    @Autowired
    ObjectMapper objectMapper; //JSON사용
    @Autowired
    MockMvc mockMvc; //요청(보내고)-응답(받기) 처리
    @MockitoBean
    PremiumService premiumService; //가짜 객체를 만들고 빈등록한다.
    @MockitoBean
    AcademyMessage academyMessage;

    final String BASE_URL = "/api/academy/premium";

    @Test
    @DisplayName("프리미엄 승인")
    void putPremiumCheck() throws Exception {
        PremiumUpdateReq givenParam = new PremiumUpdateReq();
        givenParam.setAcaId(29L);
        givenParam.setPreCheck(1);

        given(premiumService.updPremium(givenParam)).willReturn(1);

        String paramJson = objectMapper.writeValueAsString(givenParam);

        ResultActions resultActions = mockMvc.perform( put(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                                      .content(paramJson) );

        ResultResponse res = ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(1)
                .build();
        String expectedResJson = objectMapper.writeValueAsString(res);

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(premiumService).updPremium(givenParam);

    }
}