package com.green.acamatch.premium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Controller.PremiumController;
import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.premium.model.PremiumGetRes;
import com.green.acamatch.academy.premium.model.PremiumUpdateReq;
import com.green.acamatch.config.GlobalOauth2;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.model.ResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class PremiumControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PremiumService premiumService;

    @Mock
    private AcademyMessage academyMessage;

    @InjectMocks
    private PremiumController premiumController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(premiumController).build();
    }

    @Test
    void putPremiumCheck_Success() throws Exception {
        // given
        PremiumUpdateReq req = new PremiumUpdateReq();
        req.setAcaId(1L);
        req.setPreCheck(1);

        // Mocking service and message
        when(premiumService.updPremium(req)).thenReturn(1);
        when(academyMessage.getMessage()).thenReturn("승인 성공");

        // when & then
        mockMvc.perform(put("/academy/premium")
                        .contentType("application/json")
                        .content("{\"acaId\": 1, \"preCheck\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("승인 성공"))
                .andExpect(jsonPath("$.resultData").value(1));
    }


}