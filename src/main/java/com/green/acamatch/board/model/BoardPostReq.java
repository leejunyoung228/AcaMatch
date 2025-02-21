package com.green.acamatch.board.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class BoardPostReq {
    @JsonIgnore
    private Long boardId;
    @Schema(title = "학원 PK")
    private Long acaId;
    @Schema(title = "유저 PK")
    private Long userId;
    @Schema(title = "공지사항 제목", example = "2월 공지사항입니다.")
    private String boardName;
    @Schema(title = "공지사항 내용", example = "글을 적어주세요.")
    private String boardComment;
}