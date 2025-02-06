package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.AcademyService;
import com.green.acamatch.academy.Service.TagService;
import com.green.acamatch.academy.model.JW.AcademyBestLikeGetRes;
import com.green.acamatch.academy.model.JW.AcademySelOrderByLikeReq;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.model.ResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(JwtTokenProvider.class)
@WebMvcTest(
        controllers = AcademyController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class}
)
@EnableConfigurationProperties(JwtConst.class)
class AcademyControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TagService tagService;

    @MockitoBean
    AcademyMessage academyMessage;

    @MockitoBean
    AcademyService academyService;

    final String BASE_URL = "/api/academy/best";

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AcademyMessage academyMessage() {
            return Mockito.mock(AcademyMessage.class);
        }

        @Bean
        public AcademyService academyService() {
            return Mockito.mock(AcademyService.class);
        }
    }

    @Test
    @DisplayName("좋아요 순으로 학원불러오기")
    void getAcademyBest() throws Exception {
        // Given (테스트 데이터 준비)
        AcademySelOrderByLikeReq givenParam = new AcademySelOrderByLikeReq(1, 1);

        AcademyBestLikeGetRes expectedResult = new AcademyBestLikeGetRes();
        expectedResult.setAcaId(35L);
        expectedResult.setAcaName("미대편입플러스학원");
        expectedResult.setLikeCount(78);
        expectedResult.setStarAvg(4.363157894736822);
        expectedResult.setReviewCount(18);
        expectedResult.setAcaPic("b7493f57-05ba-4852-9772-f4b0f36f136c.jpg");
        expectedResult.setTagIds("103,104,111");
        expectedResult.setTagNames("편입 수학,편입 영어,미술 입시");
        expectedResult.setAcademyLikeCount(55);

        // 🔥 Mock 설정 (Mockito 사용)
        Mockito.when(academyMessage.getMessage()).thenReturn("좋아요를 많이 받은 학원 순서입니다.");
        Mockito.when(academyService.getAcademyBest(givenParam)).thenReturn(List.of(expectedResult));

        // When (API 호출)
        ResultActions resultActions = mockMvc.perform(get(BASE_URL)
                .queryParams(getParameter(givenParam)));

        // Then (검증)
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("좋아요를 많이 받은 학원 순서입니다."))
                .andExpect(jsonPath("$.resultData[0].likeCount").value(78))
                .andExpect(jsonPath("$.resultData[0].starAvg").value(4.363157894736822))
                .andExpect(jsonPath("$.resultData[0].reviewCount").value(18))
                .andExpect(jsonPath("$.resultData[0].acaPic").value("b7493f57-05ba-4852-9772-f4b0f36f136c.jpg"))
                .andExpect(jsonPath("$.resultData[0].tagIds").value("103,104,111"))
                .andExpect(jsonPath("$.resultData[0].tagNames").value("편입 수학,편입 영어,미술 입시"))
                .andExpect(jsonPath("$.resultData[0].academyLikeCount").value(55));

        Mockito.verify(academyService).getAcademyBest(givenParam);
    }

    private MultiValueMap<String, String> getParameter(AcademySelOrderByLikeReq givenParam) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", String.valueOf(givenParam.getPage()));
        queryParams.add("size", String.valueOf(givenParam.getSize()));
        return queryParams;
    }
}
