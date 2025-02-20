package com.green.acamatch.board;

import com.green.acamatch.board.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insBoard(BoardPostReq p);
    List<BoardGetDto> getBoardList(BoardGetReq p);
    int updBoard(BoardPutReq p);
    int delBoard(BoardDelReq p);
}