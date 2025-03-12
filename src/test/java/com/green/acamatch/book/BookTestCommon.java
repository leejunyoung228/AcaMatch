package com.green.acamatch.book;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.book.model.BookPostReq;
import com.green.acamatch.config.model.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class BookTestCommon {
    final ObjectMapper objectMapper;

    MultiValueMap<String, String> getParameter(MultipartFile file, BookPostReq req) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>(1);
        queryParams.add("file", String.valueOf(file));
        queryParams.add("req", String.valueOf(req));
        return queryParams;
    }

    BookPostReq getGivenParam() {
        BookPostReq givenParam = new BookPostReq();
        givenParam.setBookName("책 이름");
        givenParam.setManager("매니저");
        givenParam.setBookComment("책 소개");
        givenParam.setBookAmount(100);
        givenParam.setBookPrice(15000);
        givenParam.setClassId(1L);
        return givenParam;
    }

    String getExpectedResJson(int result) throws Exception {
        ResultResponse expectedRes = ResultResponse.<Integer>builder()
                .resultMessage("책 등록 성공")
                .resultData(result)
                .build();
        return objectMapper.writeValueAsString(expectedRes);
    }
}
