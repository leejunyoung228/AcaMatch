package com.green.acamatch.grade.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GradeStatusGetDto {
    @Schema(title = "학원 사진 여러장")
    private String acaPics;
    @Schema(title = "학원 사진")
    private String acaPic;
    @Schema(title = "시험 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long examId;
    @Schema(title = "시험 이름", example = "1차 시험")
    private String examName;
    @Schema(title = "시험 날짜", example = "2025-01-25")
    private String examDate;
    @Schema(title = "처리 상태", example = "0")
    private int processingStatus;
    @Schema(title = "유저 PK", example = "1")
    private long userId;
    @Schema(title = "선생님 이름", example = "홍길동")
    private String name;
    @Schema(title = "검색어1")
    private String keyword1;
    @Schema(title = "검색어2")
    private String keyword2;
    @Schema(title = "검색어3")
    private String keyword3;
}
