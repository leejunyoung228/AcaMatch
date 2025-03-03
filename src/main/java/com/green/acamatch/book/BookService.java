package com.green.acamatch.book;

import com.green.acamatch.academyCost.AcademyCostMapper;
import com.green.acamatch.academyCost.ProductRepository;
import com.green.acamatch.book.model.*;
import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.entity.academyCost.Book;
import com.green.acamatch.entity.academyCost.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final MyFileUtils myFileUtils;
    private final BookMessage bookMessage;
    private final ProductRepository productRepository;
    private final AcademyCostMapper academyCostMapper;

    public int postBook(MultipartFile mf, BookPostReq req) {
        Book book = new Book();
        book.setBookName(req.getBookName());
        book.setBookPrice(req.getBookPrice());
        book.setBookComment(req.getBookComment());
        Long classId = req.getClassId();
        book.setClassId(classId);
        book.setManager(req.getManager());
        book.setBookAmount(req.getBookAmount());

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
        book.setBookPic(savedPicName);

        try{
            myFileUtils.transferTo(mf, filePath);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        bookRepository.save(book);
        bookMessage.setMessage("책 등록 성공");

        Product product = new Product();
        product.setBookId(book.getBookId());
        product.setProductName(book.getBookName());
        product.setProductPrice(book.getBookPrice());
        productRepository.save(product);

        return 1;
    }

    public List<BookGetRes> getBookListByClassId(Long classId){
        List<BookGetRes> res = bookMapper.getBookListByClassId(classId);
        bookMessage.setMessage("출력 완료");
        if(res == null){
            bookMessage.setMessage("등록된 책이 없습니다.");
            return null;
        }
        return res;
    }

    public int updateBook(BookUpdateReq req, MultipartFile file) {
        if(req.getBookId() != 0){
            Book book = bookRepository.findById(req.getBookId()).orElse(null);

            if(req.getBookName() == null){
                bookMessage.setMessage("책 이름을 입력하지 않았습니다.");
                return 0;
            }
            book.setBookName(req.getBookName());

            if(req.getBookComment() == null){
                bookMessage.setMessage("책 소개를 입력하지 않았습니다.");
                return 0;
            }
            book.setBookComment(req.getBookComment());

            if(req.getManager() == null){
                bookMessage.setMessage("책 담당자를 입력하지 않았습니다.");
                return 0;
            }
            book.setManager(req.getManager());

            if(req.getClassId() == null){
                bookMessage.setMessage("책을 등록할 수업을 입력하지 않았습니다.");
                return 0;
            }
            book.setClassId(req.getClassId());

            if(req.getBookPrice() == 0){
                bookMessage.setMessage("책 가격을 입력하지 않았습니다.");
                return 0;
            }
            book.setBookPrice(req.getBookPrice());

            if(req.getBookAmount() == 0){
                bookMessage.setMessage("책 수량을 입력하지 않았습니다.");
                return 0;
            }
            book.setBookAmount(req.getBookAmount());

            if(file == null){
                bookMessage.setMessage("책 사진을 입력하지 않았습니다.");
                String originalPicName = book.getBookPic();
                book.setBookPic(originalPicName);
                bookRepository.save(book);

                long productId = bookMapper.getProductIdByBookId(req.getBookId());
                Product product = productRepository.findById(productId).orElse(null);
                product.setProductName(req.getBookName());
                product.setProductPrice(req.getBookPrice());
                productRepository.save(product);

                return 1;
            }
            String savedPicName = myFileUtils.makeRandomFileName(file);
            String middlePath = String.format("book/%d", req.getBookId());
            String filePath = String.format("%s/%s", middlePath, savedPicName);
            try{
                myFileUtils.deleteFolder(middlePath, true);
                myFileUtils.makeFolders(middlePath);
                book.setBookPic(savedPicName);
                myFileUtils.transferTo(file, filePath);
            } catch (IOException e){
                e.printStackTrace();
            }

            bookRepository.save(book);
        }

        long productId = bookMapper.getProductIdByBookId(req.getBookId());
        Product product = productRepository.findById(productId).orElse(null);
        product.setProductName(req.getBookName());
        product.setProductPrice(req.getBookPrice());
        productRepository.save(product);

        bookMessage.setMessage("수정 완료");
        return 1;
    }

    public int deleteBook(Long id) {
        String filePath = String.format("book/%d", id);
        myFileUtils.deleteFolder(filePath, true);
        bookRepository.deleteById(id);
        return 1;
    }

    public List<GetBookListByAcaNameBookNameRes> getBookListByAcaNameBookName(GetBookListByAcaNameBookNameReq req){
        List<GetBookListByAcaNameBookNameRes> res = bookMapper.getBookListByAcaNameBookName(req);
        if(res == null){
            bookMessage.setMessage("검색된 조건에 맞는 교재가 없습니다.");
            return null;
        }
        return res;
    }

    public GetBookInfo getBookInfo(long bookId){
        return bookMapper.getBookInfo(bookId);
    }
}
