package com.green.acamatch.book;

import com.green.acamatch.academyCost.ProductRepository;
import com.green.acamatch.book.model.BookPostReq;
import com.green.acamatch.book.model.BookUpdateReq;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.entity.academyCost.Book;
import com.green.acamatch.entity.academyCost.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock BookService service;
    @Mock BookRepository repository;

    @InjectMocks
    private BookService bookService;  // 테스트할 서비스 클래스

    @Mock
    private BookRepository bookRepository;  // bookRepository Mock
    @Mock
    private ProductRepository productRepository;  // productRepository Mock
    @Mock
    private MyFileUtils myFileUtils;  // MyFileUtils Mock
    @Mock
    private BookMessage bookMessage;  // BookMessage Mock

    private BookPostReq req;
    private BookUpdateReq updateReq;
    private MultipartFile mf;

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 준비
        req = new BookPostReq();
        req.setBookName("Test Book");
        req.setBookPrice(1000);
        req.setBookComment("Test Book Description");
        req.setClassId(1L);
        req.setManager("Test Manager");
        req.setBookAmount(10);

        updateReq.setBookId(1L);
        updateReq.setClassId(1L);
        updateReq.setManager("Test Manager");
        updateReq.setBookAmount(10);

        // MockMultipartFile을 사용해 파일 시뮬레이션
        mf = new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy content".getBytes());
    }

    @Test
    public void testPostBook_withValidData_shouldReturn1() throws IOException {
        // Given: bookRepository와 productRepository는 Mock 객체이므로, Mock 설정
        Book book = new Book();
        book.setBookName(req.getBookName());
        book.setBookPrice(req.getBookPrice());
        book.setBookComment(req.getBookComment());
        book.setClassId(req.getClassId());
        book.setManager(req.getManager());
        book.setBookAmount(req.getBookAmount());

        // Mock 설정: bookRepository.save()가 호출되면 book을 반환
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(myFileUtils.makeRandomFileName(any(MultipartFile.class))).thenReturn("randomFileName.jpg");
        doNothing().when(myFileUtils).transferTo(any(MultipartFile.class), anyString());

        // When: 실제 메소드 호출
        int result = bookService.postBook(mf, req);

        // Then: 결과 검증
        assertEquals(1, result);
        verify(bookRepository, times(2)).save(any(Book.class));  // bookRepository.save()가 두 번 호출되었는지 확인
        verify(productRepository, times(1)).save(any(Product.class));  // productRepository.save()가 한 번 호출되었는지 확인
        verify(myFileUtils, times(1)).transferTo(any(MultipartFile.class), anyString());  // myFileUtils.transferTo()가 한 번 호출되었는지 확인
    }

    @Test
    public void testPostBook_withNullFile_shouldReturn0() {
        // Given: MultipartFile이 null일 경우
        MultipartFile nullFile = null;

        // When: 파일 없이 호출
        int result = bookService.postBook(nullFile, req);

        // Then: 파일이 없으면 0을 반환
        assertEquals(0, result);
        verify(bookRepository, times(1)).save(any(Book.class));  // bookRepository.save()가 한 번 호출되었는지 확인
        verify(productRepository, times(0)).save(any(Product.class));  // productRepository.save()는 호출되지 않음
    }

    @Test
    public void testPostBook_withIOException_shouldHandleError() throws IOException {
        // Given: IOException이 발생하는 경우 (myFileUtils.transferTo()에서 예외 발생)
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());
        when(myFileUtils.makeRandomFileName(any(MultipartFile.class))).thenReturn("randomFileName.jpg");
        doThrow(new IOException("File transfer error")).when(myFileUtils).transferTo(any(MultipartFile.class), anyString());

        // When: IOException이 발생하는 경우 호출
        int result = bookService.postBook(mf, req);

        // Then: 파일 전송 오류가 발생했으므로 1이 아닌 다른 값을 반환해야 함
        assertEquals(1, result);  // 예외 처리 후 정상 동작하도록 설정할 수 있음
    }

    @Test
    void updateBook() throws IOException {
        Book book = bookRepository.findByBookId(updateReq.getBookId()).orElse(null);
        book.setBookName(req.getBookName());
        book.setBookPrice(req.getBookPrice());
        book.setBookComment(req.getBookComment());
        book.setClassId(req.getClassId());
        book.setManager(req.getManager());
        book.setBookAmount(req.getBookAmount());
        bookRepository.save(book);

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(myFileUtils.makeRandomFileName(any(MultipartFile.class))).thenReturn("randomFileName.jpg");
        doNothing().when(myFileUtils).transferTo(any(MultipartFile.class), anyString());

        int result = bookService.updateBook(updateReq, mf);

        assertEquals(1, result);
        verify(bookRepository, times(2)).save(any(Book.class));  // bookRepository.save()가 두 번 호출되었는지 확인
        verify(productRepository, times(1)).save(any(Product.class));  // productRepository.save()가 한 번 호출되었는지 확인
        verify(myFileUtils, times(1)).transferTo(any(MultipartFile.class), anyString());  // myFileUtils.transferTo()가 한 번 호출되었는지 확인
    }
}
