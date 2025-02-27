package com.green.acamatch.board.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BoardGetListDto {
    @Schema(title = "공지사항 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long boardId;
    @Schema(title = "학원 PK")
    private Long acaId; //null 허용
    @Schema(title = "학원 이름")
    private String acaName;
    @Schema(title = "유저 PK")
    private Long userId; //null 허용
    @Schema(title = "공지사항 제목", example = "2월 공지사항입니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String boardName;
    @Schema(title = "등록 날짜", example = "2025-02-24", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate createdAt;
    @Schema(title = "이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}