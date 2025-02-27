package com.green.acamatch.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardGetDetailReq {
    @Schema(title = "공지사항 PK", requiredMode = Schema.RequiredMode.REQUIRED)
    private long boardId;
    @JsonIgnore
    private Long userId; //null 허용
    @JsonIgnore
    private Long acaId; //null 허용
}