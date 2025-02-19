package com.green.acamatch.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BoardPostReq {
    @JsonIgnore
    private long boardId;
    @Schema(title = "학원 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long acaId;
    @Schema(title = "유저 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    @Schema(title = "공지사항 제목", example = "2월 공지사항입니다.")
    private String boardName;
    @Schema(title = "공지사항 내용", example = "글을 적어주세요.")
    private String boardComment;
    @Schema(title = "작성 날짜", example = "2025-02-19 17:27:11")
    private LocalDateTime createdAt;
}