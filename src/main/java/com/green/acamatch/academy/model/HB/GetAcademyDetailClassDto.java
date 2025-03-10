package com.green.acamatch.academy.model.HB;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
public class GetAcademyDetailClassDto {
    @Schema(title = "수업 PK")
    private long classId;
    @Schema(title = "수업 이름")
    private String className;
    @Schema(title = "수업 설명")
    private String classComment;
    @Schema(title = "수업 시작 날짜")
    private String classStartDate;
    @Schema(title = "수업 종료 날짜")
    private String classEndDate;
    @Schema(title = "수업 시작 시간")
    private String classStartTime;
    @Schema(title = "수업 종료 시간")
    private String classEndTime;
    @Schema(title = "수업 가격")
    private Integer classPrice;
    @Schema(title = "수업하는 요일")
    private String classDay;
    @Schema(title = "수업 카테고리")
    private String classCategoryName;
    private Integer userCertification;
    @Schema(title = "수업에 맞는 productId")
    private long productId;
}