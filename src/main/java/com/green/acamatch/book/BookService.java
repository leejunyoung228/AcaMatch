package com.green.acamatch.book;

import com.green.acamatch.book.model.BookGetRes;
import com.green.acamatch.book.model.BookPostReq;
import com.green.acamatch.book.model.BookUpdateReq;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.entity.academyCost.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final MyFileUtils myFileUtils;
    private final BookMessage bookMessage;

    public int postBook(MultipartFile mf, BookPostReq req) {
        Book book = new Book();
        book.setBookName(req.getBookName());
        book.setBookPrice(req.getBookPrice());
        book.setBookComment(req.getBookComment());
        Long classId = req.getClassId();
        book.setClassId(classId);
        book.setManager(req.getManager());

        bookRepository.save(book);

        long bookId = book.getBookId();

        if(mf == null){
            bookMessage.setMessage("사진이 필요합니다.");
            return 0;
        }
        String middlePath = String.format("book/%d", bookId);
        myFileUtils.makeFolders(middlePath);

        String savedPicName = mf != null ? myFileUtils.makeRandomFileName(mf) : null;


        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try{
            book.setBookPic(savedPicName);
            myFileUtils.transferTo(mf, filePath);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        bookRepository.save(book);
        bookMessage.setMessage("책 등록 성공");
        return 1;
    }

    public List<BookGetRes> getBookListByClassId(Long classId){
        return bookMapper.getBookListByClassId(classId);
    }

    public int updateBook(BookUpdateReq req) {
        if(req.getBookId() != 0){
            Book book = bookRepository.findById(req.getBookId()).orElse(null);

            if(req.getBookName() == null){
                bookMessage.setMessage("책 이름을 입력하지 않았습니다.");
                return 0;
            }
            book.setBookName(req.getBookName());

            if(req.getBookPic() == null){
                bookMessage.setMessage("책 사진을 입력하지 않았습니다.");
                return 0;
            }
            book.setBookPic(req.getBookPic());

            if(req.getBookComment() != null){
                bookMessage.setMessage("책 소개를 입력하지 않았습니다.");
                return 0;
            }
            book.setBookComment(req.getBookComment());

            if(req.getManager() != null){
                bookMessage.setMessage("책 담당자를 입력하지 않았습니다.");
                return 0;
            }
            book.setManager(req.getManager());

            if(req.getClassId() != null){
                bookMessage.setMessage("책을 등록할 수업을 입력하지 않았습니다.");
                return 0;
            }
            book.setClassId(req.getClassId());

            if(req.getBookPrice() != 0){
                bookMessage.setMessage("책 가격을 입력하지 않았습니다.");
                return 0;
            }
            book.setBookPrice(req.getBookPrice());

            bookRepository.save(book);
        }
        return 1;
    }

    public int deleteBook(Long id) {
        String filePath = String.format("book/%d", id);
        myFileUtils.deleteFolder(filePath, true);
        bookRepository.deleteById(id);
        return 1;
    }
}
