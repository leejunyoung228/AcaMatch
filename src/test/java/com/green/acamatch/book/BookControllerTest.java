package com.green.acamatch.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.acamatch.book.model.BookGetRes;
import com.green.acamatch.book.model.BookPostReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = BookController.class
        , excludeAutoConfiguration = { SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class }
)
class BookControllerTest {
    @Autowired ObjectMapper objectMapper; //JSON사용
    @Autowired MockMvc mockMvc; //요청(보내고)-응답(받기) 처리
    @MockitoBean BookService service; //가짜 객체를 만들고 빈등록한다.

    final String BASE_URL = "/api/book";
    BookPostReq req = new BookPostReq();
    MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
    BookTestCommon common;
    @Test
    void postBook() throws Exception {
        given(service.postBook(file, req)).willReturn(1);

        postBook(1);
    }

    @Test
    void getBookListByClassId() {
        List<BookGetRes> givenParam = service.getBookListByClassId(1L);
        given(service.getBookListByClassId(1L)).willReturn(givenParam);
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void getBookListByAcaNameBookName() {
    }

    @Test
    void getBookInfo() {
    }

    private void postBook(final int result) throws Exception {
        given(service.postBook(file, req)).willReturn(1);

        ResultActions resultActions = mockMvc.perform(  get(BASE_URL).queryParams(common.getParameter(file, req))  );

        String expectedResJson = common.getExpectedResJson(result);
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson));


        verify(service).postBook(file, req);
    }
}