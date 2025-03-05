package com.green.acamatch.grade.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GradeDetailDto {

    @Schema(title = "사용자 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long userId;
    @Schema(title = "유저 사진")
    private String userPic;
    @Schema(title = "유저 이름", example = "홍길동")
    private String userName;
    @Schema(title = "수강생 PK", example = "1")
    private long joinClassId;
    @Schema(title = "성적 PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private long gradeId;
    @Schema(title = "시험 이름", example = "1회 모의고사")
    private String examName;
    @Schema(title = "시험 날짜", example = "2025-01-24")
    private LocalDate examDate;
    @Schema(title = "성적", example = "90")
    private Integer score;
    @Schema(title = "통과 여부", example = "null")
    private Integer pass;
}
