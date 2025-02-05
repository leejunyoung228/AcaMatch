package com.green.acamatch.grade.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GradeUserDto {

    private long gradeId;
    @Schema(title = "유저 사진")
    private String userPic;
    @Schema(title = "유저 이름", example = "홍길동")
    private String userName;
    @Schema(title = "시험 날짜", example = "2025-01-24")
    private String examDate;
    @Schema(title = "성적", example = "90")
    private Integer score;
    @Schema(title = "통과 여부", example = "null")
    private Integer pass;
}
