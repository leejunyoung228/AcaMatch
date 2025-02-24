package com.green.acamatch.book.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookGetReq {
    @Schema(title = "수업 PK", example = "1")
    private Long classId;
}
