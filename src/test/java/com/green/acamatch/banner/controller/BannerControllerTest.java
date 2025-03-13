package com.green.acamatch.banner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Controller.BannerController;
import com.green.acamatch.academy.Service.BannerService;
import com.green.acamatch.academy.banner.model.BannerPostReq;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.GlobalOauth2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BannerControllerTest {

    @Mock
    private BannerService bannerService;

    @Mock
    private AcademyMessage academyMessage;

    @InjectMocks
    private BannerController bannerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bannerController).build();
    }

    @Test
    public void postBannerTest() throws Exception {
        // Given
        BannerPostReq bannerPostReq = new BannerPostReq();
        bannerPostReq.setAcaId(2L);  // 샘플 데이터 설정

        // Mocking the service method and academyMessage
        when(academyMessage.getMessage()).thenReturn("배너 신청 성공");
        when(bannerService.postBanner(any(), any(), any(), eq(bannerPostReq))).thenReturn(1);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/banner")
                        .file("topBannerPic", new byte[]{})  // 가짜 파일 데이터
                        .file("bottomBannerPic", new byte[]{})
                        .file("rightBannerPic", new byte[]{})
                        .param("acaId", "2")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultMessage").value("배너 신청 성공"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.resultData").value(1));

        // verify that the service method was called
        verify(bannerService, times(1)).postBanner(any(), any(), any(), eq(bannerPostReq));
    }
}