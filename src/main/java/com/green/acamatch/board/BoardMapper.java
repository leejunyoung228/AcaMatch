package com.green.acamatch.board;

import com.green.acamatch.board.model.BoardPostReq;
import com.green.acamatch.entity.board.Board;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    int insBoard(BoardPostReq p);
}