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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
class BannerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BannerService bannerService;

    @Mock
    private AcademyMessage academyMessage;

    @InjectMocks
    private BannerController bannerController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bannerController).build();
    }

    @Test
    void postBanner_Success() throws Exception {
        // given
        BannerPostReq req = new BannerPostReq();
        req.setAcaId(1L); // acaId 필드 설정

        // Mocking service and message
        when(bannerService.postBanner(any(), any(), any(), eq(req))).thenReturn(1); // int 값을 반환하도록 수정
        when(academyMessage.getMessage()).thenReturn("배너 신청 성공");

        // when & then
        mockMvc.perform(multipart("/banner")
                        .file(new MockMultipartFile("topBannerPic", "topBannerPic.jpg", "image/jpeg", new byte[0])) // 가짜 파일
                        .file(new MockMultipartFile("bottomBannerPic", "bottomBannerPic.jpg", "image/jpeg", new byte[0]))
                        .file(new MockMultipartFile("rightBannerPic", "rightBannerPic.jpg", "image/jpeg", new byte[0]))
                        .param("acaId", "1") // acaId 파라미터 추가
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("배너 신청 성공"))
                .andExpect(jsonPath("$.resultData").value(1));
    }
}