package com.green.acamatch.book;

import com.green.acamatch.book.model.BookGetRes;
import com.green.acamatch.book.model.GetBookInfo;
import com.green.acamatch.book.model.GetBookListByAcaNameBookNameReq;
import com.green.acamatch.book.model.GetBookListByAcaNameBookNameRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    List<BookGetRes> getBookListByClassId(Long classId);
    List<GetBookListByAcaNameBookNameRes> getBookListByAcaNameBookName(GetBookListByAcaNameBookNameReq req);
    GetBookInfo getBookInfo(long bookId);
}
