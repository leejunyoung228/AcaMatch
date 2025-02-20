package com.green.acamatch.board.model;

import com.green.acamatch.config.model.Paging;
import com.green.acamatch.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BoardGetDto extends Paging {
    private long acaId;
    private long userId;
    private long boardId;
    private String boardName;
    private LocalDateTime createdAt;

    public BoardGetDto(Integer page, Integer size) {
        super(page, size);
    }
}
