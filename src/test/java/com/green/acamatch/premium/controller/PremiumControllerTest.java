package com.green.acamatch.premium.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Controller.PremiumController;
import com.green.acamatch.academy.Service.PremiumService;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.premium.model.PremiumBannerGetRes;
import com.green.acamatch.academy.premium.model.PremiumGetRes;
import com.green.acamatch.academy.premium.model.PremiumUpdateReq;
import com.green.acamatch.config.model.ResultResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@WebMvcTest(PremiumController.class)
class PremiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private PremiumService premiumService;

    @Mock
    private AcademyMessage academyMessage;

    @InjectMocks
    private PremiumController premiumController;

    private PremiumUpdateReq updateReq;
    private PremiumGetRes premiumGetRes;


    @BeforeEach
    public void setUp1() {
        updateReq = new PremiumUpdateReq();
        updateReq.setPreCheck(1);  // 가정: 요청의 preCheck 값은 1로 설정
    }

    @BeforeEach
    public void setUp2() {
        premiumGetRes = new PremiumGetRes();
        premiumGetRes.setAcaId(1L);
        premiumGetRes.setAcaName("HUMAN Academy");
    }

    //프리미엄승인
    @Test
    public void putPremiumCheck_승인성공() {
        // Mocking PremiumService의 updPremium 메소드
        when(premiumService.updPremium(any(PremiumUpdateReq.class))).thenReturn(1); // 1을 리턴한다고 가정

        // Mocking AcademyMessage
        when(academyMessage.getMessage()).thenReturn("승인 완료");

        // Controller 메소드 호출
        ResultResponse<Integer> response = premiumController.putPremiumCheck(updateReq);

        // 결과 검증
        assertEquals("승인 완료", response.getResultMessage());
        assertEquals(1, response.getResultData());
    }

    @Test
    public void putPremiumCheck_승인실패() {
        // 실패 케이스: 예를 들어 0을 반환하도록 설정
        when(premiumService.updPremium(any(PremiumUpdateReq.class))).thenReturn(0);

        // Mocking AcademyMessage
        when(academyMessage.getMessage()).thenReturn("승인 실패");

        // Controller 메소드 호출
        ResultResponse<Integer> response = premiumController.putPremiumCheck(updateReq);

        // 결과 검증
        assertEquals("승인 실패", response.getResultMessage());
        assertEquals(0, response.getResultData());
    }


    //프리미엄 학원 조회
    @Test
    public void getPremiumAcademy_조회성공() {
        // 준비
        List<PremiumGetRes> resList = Arrays.asList(premiumGetRes);

        // Mocking PremiumService의 getPremium 메소드
        when(premiumService.getPremium(Mockito.any(Pageable.class))).thenReturn(resList);

        // Mocking AcademyMessage
        when(academyMessage.getMessage()).thenReturn("성공");

        // Controller 메소드 호출
        ResultResponse<List<PremiumGetRes>> response = premiumController.getPremiumAcademy(1, 10);

        // 검증
        assertEquals("성공", response.getResultMessage());
        assertEquals(1, response.getResultData().size());
        assertEquals("HUMAN Academy", response.getResultData().get(0).getAcaName());
    }

    @Test
    public void getPremiumAcademy_조회실패() {
        // 준비
        when(premiumService.getPremium(Mockito.any(Pageable.class))).thenReturn(Arrays.asList());

        // Mocking AcademyMessage
        when(academyMessage.getMessage()).thenReturn("No Data");

        // Controller 메소드 호출
        ResultResponse<List<PremiumGetRes>> response = premiumController.getPremiumAcademy(1, 10);

        // 검증
        assertEquals("No Data", response.getResultMessage());
        assertEquals(0, response.getResultData().size());
    }


    //프리미엄학원, 배너타입 조회
    @Test
    public void getPremiumBannerTypeAcademy_조회성공() throws Exception {
        mockMvc.perform(get("/academy/premium/bannerType")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // HTTP 상태 코드 확인
                .andExpect(jsonPath("$.resultMessage").value("성공"))  // 결과 메시지 확인
                .andExpect(jsonPath("$.resultData[0].acaName").value("HUMAN Academy"))  // 첫 번째 결과의 acaName 확인
                .andExpect(jsonPath("$.resultData[0].bannerType").value("Standard"));  // 첫 번째 결과의 bannerType 확인
    }

    @Test
    public void getPremiumBannerTypeAcademy_조회실패() throws Exception {
        mockMvc.perform(get("/academy/premium/bannerType")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // HTTP 상태 코드 확인
                .andExpect(jsonPath("$.resultMessage").value("No Data"))  // 결과 메시지 확인
                .andExpect(jsonPath("$.resultData").isEmpty());  // 결과 데이터가 비어있는지 확인
    }
}
