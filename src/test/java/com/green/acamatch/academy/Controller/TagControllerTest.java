package com.green.acamatch.academy.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.academy.Service.TagService;
import com.green.acamatch.academy.tag.SelTagDto;
import com.green.acamatch.academy.tag.SelTagReq;
import com.green.acamatch.academy.tag.SelTagRes;
import com.green.acamatch.config.model.ResultResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(
        controllers = TagController.class
        , excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class TagControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Mock
    TagService tagService;

    final String BASE_URL = "/api/academy/tag";

    @Test
    @DisplayName("태그 불러오기")
    void selTagList() throws Exception{
        SelTagReq givenParam = new SelTagReq();
        givenParam.setSearchTag("국어");


        SelTagRes expectedResult = new SelTagRes();
        expectedResult.setSelTagList(List.of(new SelTagDto()));

        given(tagService.selTagList(givenParam)).willReturn(expectedResult);
        ResultActions resultActions;

        resultActions = mockMvc.perform(get(BASE_URL).queryParams(getParameter(givenParm)));

    String expectedResJson;
    expectedResJson = getExpectedResJson(expectedResult);

    }

    private MultiValueMap<String, String> getParameter(SelTagReq givenParam) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>(3);

        queryParams.add("searchTag", String.valueOf(givenParam.getSearchTag()));
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