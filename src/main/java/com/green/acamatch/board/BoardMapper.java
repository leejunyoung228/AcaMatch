package com.green.acamatch.board;

import com.green.acamatch.board.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insBoard(BoardPostReq p);
    List<BoardGetDto> getBoardList(BoardGetDetailReq p); //리스트 불러오기
    List<BoardGetDetailRes> getBoardDetail(BoardGetDetailReq p); //상세보기
    int updBoard(BoardPutReq p);
    int delBoard(BoardDelReq p);
}