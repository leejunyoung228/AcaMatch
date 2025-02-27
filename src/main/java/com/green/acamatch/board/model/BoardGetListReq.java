package com.green.acamatch.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardGetListReq extends Paging {
    @JsonIgnore
    private long boardId;
    @Schema(title = "유저 PK")
    private Long userId; //null 허용
    @Schema(title = "학원 PK")
    private Long acaId; //null 허용

    private String keyword;

    public BoardGetListReq(Integer page, Integer size, Long userId, Long acaId, String keyword) {
        super(page, size);
        this.userId = userId;
        this.acaId = acaId;
        this.keyword = keyword;
    }
}
