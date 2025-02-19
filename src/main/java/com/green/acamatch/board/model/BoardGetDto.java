package com.green.acamatch.board.model;

import com.green.acamatch.config.model.Paging;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BoardGetDto extends Paging {
    private String boardName;
    private LocalDateTime createdAt;
    private String name;

    public BoardGetDto(Integer page, Integer size) {
        super(page, size);
    }
}
