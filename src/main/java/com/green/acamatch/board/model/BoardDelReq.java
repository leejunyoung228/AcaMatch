package com.green.acamatch.board.model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardDelReq {
    @Schema(title = "공지사항 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long boardId;
    @Schema(title = "유저 PK")
    private Long userId;
    @Schema(title = "학원 PK")
    private Long acaId;
}