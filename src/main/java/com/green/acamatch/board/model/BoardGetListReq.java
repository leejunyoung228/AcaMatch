package com.green.acamatch.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardGetListReq {
    @JsonIgnore
    private long boardId;
    @Schema(title = "유저 PK")
    private Long userId; //null 허용
    @Schema(title = "학원 PK")
    private Long acaId; //null 허용

}
