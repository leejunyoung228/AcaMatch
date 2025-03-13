package com.green.acamatch.banner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Controller.BannerController;
import com.green.acamatch.academy.Service.BannerService;
import com.green.acamatch.academy.banner.model.BannerPostReq;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BannerController.class)
@ExtendWith(SpringExtension.class)
class BannerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BannerService bannerService;

    @MockitoBean
    AcademyMessage academyMessage;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("배너 신청 - 성공")
    void postBanner_Success() throws Exception {
        // given
        MultipartFile topBannerPic = mock(MultipartFile.class);
        MultipartFile bottomBannerPic = mock(MultipartFile.class);
        MultipartFile rightBannerPic = mock(MultipartFile.class);

        BannerPostReq req = new BannerPostReq();
        req.setAcaId(2L); // req에 필요한 필드를 설정합니다.

        given(academyMessage.getMessage()).willReturn("배너 신청이 완료되었습니다.");

        // when & then
        mockMvc.perform(multipart("/banner")
                        .file("topBannerPic", new byte[0])  // 임의의 파일 데이터를 넣어줍니다.
                        .file("bottomBannerPic", new byte[0])
                        .file("rightBannerPic", new byte[0])
                        .param("someField", "value") // BannerPostReq의 파라미터를 설정
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("배너 신청이 완료되었습니다."))
                .andExpect(jsonPath("$.resultData").value(1));

        // verify that the service method was called
        verify(bannerService).postBanner(any(), any(), any(), eq(req));
    }

    @Test
    @DisplayName("배너 신청 - 실패 (파일이 없을 경우)")
    void postBanner_Fail_NoFiles() throws Exception {
        // given
        BannerPostReq req = new BannerPostReq();
        req.setAcaId(2L); // req에 필요한 필드를 설정합니다.

        given(academyMessage.getMessage()).willReturn("배너 신청이 실패했습니다.");

        // when & then
        mockMvc.perform(multipart("/banner")
                        .param("someField", "value") // 파일 없이 req 파라미터만
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())  // 실패 시 상태 코드
                .andExpect(jsonPath("$.resultMessage").value("배너 신청이 실패했습니다."))
                .andExpect(jsonPath("$.resultData").value(0));

        // verify that the service method was not called
        verify(bannerService, never()).postBanner(any(), any(), any(), eq(req));
    }
}