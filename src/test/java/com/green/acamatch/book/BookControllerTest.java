package com.green.acamatch.book;

import com.green.acamatch.academy.Service.CSDService;
import com.green.acamatch.book.model.BookGetRes;
import com.green.acamatch.book.model.BookPostReq;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class BookControllerTest {
    @Mock BookService service;

    @Test
    void postBook() {
        BookPostReq givenParam = new BookPostReq();
        givenParam.setBookName("테스트 1");
        givenParam.setBookComment("책 설명");
        givenParam.setBookAmount(100);
        givenParam.setBookPrice(10000);
        givenParam.setManager("테스트 매니저");
        givenParam.setClassId(1L);
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());

        given(service.postBook(file, givenParam)).willReturn(1);

        int result = service.postBook(file, givenParam);

        assertEquals(1, result);
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
}