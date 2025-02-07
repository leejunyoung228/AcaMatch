package com.green.acamatch.academy.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Service.TagService;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.academy.tag.SelTagDto;
import com.green.acamatch.academy.tag.SelTagReq;
import com.green.acamatch.academy.tag.SelTagRes;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.constant.JwtConst;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.model.ResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.containsString;



@Import(JwtTokenProvider.class)
@WebMvcTest(
        controllers = TagController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@EnableConfigurationProperties(JwtConst.class)
class TagControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    private AcademyMessage academyMessage;
    @MockitoBean
    TagService tagService;

    final long tagId_9 = 9L;
    final String tagName_9 = "고등수학";
    final String BASE_URL = "/api/tag";

    @Test
    @DisplayName("태그 불러오기")
    void selTagList() throws Exception {
        // Given (테스트 데이터 준비)
        SelTagReq givenParam = new SelTagReq();
        givenParam.setSearchTag(tagName_9);

        SelTagDto selTagDto = new SelTagDto();
        selTagDto.setTagId(tagId_9);
        selTagDto.setTagName(tagName_9);

        SelTagRes expectedResult = new SelTagRes();
        expectedResult.setSelTagList(List.of(selTagDto));

        // resultMessage를 mock으로 설정
        given(academyMessage.getMessage()).willReturn("1 rows");

        given(tagService.selTagList(givenParam)).willReturn(expectedResult);

        // When (API 호출)
        ResultActions resultActions = mockMvc.perform(get(BASE_URL)
                .queryParams(getParameter(givenParam)));

        // Then (검증)
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultMessage").value("1 rows")) // 메시지 검증
                .andExpect(jsonPath("$.resultData.selTagList[0].tagId").value(tagId_9)) // 태그 ID 검증
                .andExpect(jsonPath("$.resultData.selTagList[0].tagName").value(tagName_9)); // 태그 이름 검증

        verify(tagService).selTagList(givenParam);
    }

    private MultiValueMap<String, String> getParameter(SelTagReq givenParam) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>(1);
        queryParams.add("searchTag", givenParam.getSearchTag());
        return queryParams;
    }

    private String getExpectedResJson(SelTagRes res) throws Exception {
        ResultResponse expectedRes = ResultResponse.<SelTagRes>builder()
                .resultMessage(String.format("%d rows", res.getSelTagList().size()))
                .resultData(res)
                .build();
        return objectMapper.writeValueAsString(expectedRes); //object를 json형태의 문자열로 바꿈
    }
}