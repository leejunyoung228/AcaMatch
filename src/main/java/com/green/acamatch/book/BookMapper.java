package com.green.acamatch.book;

import com.green.acamatch.book.model.BookGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    List<BookGetRes> getBookListByClassId(Long classId);
}
