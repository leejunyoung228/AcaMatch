package com.green.acamatch.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardDelReq {
    @JsonIgnore
    private long userId;
    @JsonIgnore
    private long acaId;
    @Schema(title = "공지사항 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long boardId;
}