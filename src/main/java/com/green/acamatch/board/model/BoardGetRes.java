package com.green.acamatch.board.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BoardGetRes {
    List<BoardGetDto> boardGetDtoList;
}