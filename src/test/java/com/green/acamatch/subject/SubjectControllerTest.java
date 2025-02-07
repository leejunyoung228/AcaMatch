package com.green.acamatch.subject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.AcaMatchApplication;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.subject.model.SubjectPostReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JwtTokenProvider.class)
@WebMvcTest(
        controllers = SubjectController.class
        , excludeAutoConfiguration = SecurityAutoConfiguration.class)
@EnableConfigurationProperties(JwtConst.class)
class SubjectControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    AcademyMessage academyMessage;

    @MockitoBean
    SubjectService subjectService;

    final long CLASS_ID_2 = 2L;
    final long SUBJECT_ID_6 = 6L;
    final String BASE_URL = "/api/subject";

    @Test
    @DisplayName("시험 이름 등록 테스트")
    void postSubject() throws Exception {
        SubjectPostReq givenParam = new SubjectPostReq();
        givenParam.setClassId(CLASS_ID_2);
        givenParam.setSubjectId(SUBJECT_ID_6);
        givenParam.setSubjectName("2회 국어 모의고사");
        givenParam.setScoreType(0);

        // resultMessage를 mock으로 설정
        Mockito.when(academyMessage.getMessage()).thenReturn("시험 회차 등록에 성공하였습니다.");

        String paramJson = objectMapper.writeValueAsString(givenParam);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(paramJson));

        ResultResponse res = ResultResponse.<Long>builder()
                .resultMessage("시험 회차 등록에 성공하였습니다.")
                .resultData(SUBJECT_ID_6)
                .build();

        String expectedResJson = objectMapper.writeValueAsString(res);
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        Mockito.verify(subjectService).postSubject(givenParam);
    }
}