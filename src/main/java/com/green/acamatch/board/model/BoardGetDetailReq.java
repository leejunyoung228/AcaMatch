package com.green.acamatch.board.model;

import com.green.acamatch.config.model.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardGetDetailReq extends Paging {
    @Schema(title = "공지사항 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long boardId;
    @Schema(title = "유저 PK")
    private Long userId; //null 허용
    @Schema(title = "학원 PK")
    private Long acaId; //null 허용

    public BoardGetDetailReq(Integer page, Integer size) {
        super(page, size);
    }
}
