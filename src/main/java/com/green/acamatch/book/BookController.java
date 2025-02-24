package com.green.acamatch.book;

import com.green.acamatch.book.model.BookGetRes;
import com.green.acamatch.book.model.BookPostReq;
import com.green.acamatch.book.model.BookUpdateReq;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.entity.academyCost.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Tag(name = "책")
public class BookController {
    private final BookService bookService;
    private final BookMessage bookMessage;

    @PostMapping
    @Operation(summary = "책 등록")
    public ResultResponse<Integer> postBook(@RequestPart BookPostReq req,
                                            @RequestPart MultipartFile file) {
        int result = bookService.postBook(file, req);
        return ResultResponse.<Integer>builder()
                .resultData(result)
                .resultMessage(bookMessage.getMessage())
                .build();
    }

    @GetMapping("getBookList/{classId}")
    @Operation(summary = "수업에 맞는 책 출력")
    public ResultResponse<List<BookGetRes>> getBookListByClassId(@PathVariable Long classId){
        List<BookGetRes> bookList = bookService.getBookListByClassId(classId);
        return ResultResponse.<List<BookGetRes>>builder()
                .resultMessage("출력 완료")
                .resultData(bookList)
                .build();
    }

    @PutMapping("updateBook")
    @Operation(summary = "책 정보 수정")
    public ResultResponse<Integer> updateBook(@ParameterObject BookUpdateReq req){
        int result = bookService.updateBook(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(bookMessage.getMessage())
                .resultData(result)
                .build();
    }

    @DeleteMapping("deleteBook")
    @Operation(summary = "책 삭제")
    public ResultResponse<Integer> deleteBook(@RequestParam Long bookId){
        int result = bookService.deleteBook(bookId);
        return ResultResponse.<Integer>builder()
                .resultMessage("삭제 완료")
                .resultData(result)
                .build();
    }
}
