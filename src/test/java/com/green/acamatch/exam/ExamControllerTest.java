package com.green.acamatch.exam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.GlobalOauth2;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.exam.model.ExamPostReq;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExamController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties.class
        })
@Import({SecurityConfig.class, GlobalOauth2.class})
public class ExamControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ExamService examService;

    final long CLASS_ID_2 = 2L;
    final long EXAM_ID_1 = 1L;
    final String BASE_URL = "/api/exam";

    @Test
    @DisplayName("시험 등록 테스트")
    void postExam() throws Exception {
        ExamPostReq givenParam = new ExamPostReq();
        givenParam.setClassId(CLASS_ID_2);
        givenParam.setExamName("학원 모의고사 1차");
        givenParam.setExamType(0);

        given(examService.postExam(givenParam)).willReturn((int) EXAM_ID_1);

        String paramJson = objectMapper.writeValueAsString(givenParam);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(paramJson));

        ResultResponse res = ResultResponse.<Long>builder()
                .resultMessage("시험 등록에 성공하였습니다. 앙 기모찌")
                .resultData(EXAM_ID_1)
                .build();
        String expectedResJson = objectMapper.writeValueAsString(res);

        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));

        verify(examService).postExam(givenParam);
    }
}