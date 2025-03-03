package com.green.acamatch.popUp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PopUpGetDto {
    @Schema(title = "팝업 제목", example = "아카매치 GRAND OPEN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(title = "시작 날짜", example = "2025-02-21", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate startDate;
    @Schema(title = "종료 날짜", example = "2025-02-22", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate endDate;
    @Schema(title = "활성/비활성", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int popUpShow;
    @Schema(title = "대상",example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int popUpType;
    @Schema(title = "팝업 ID PK", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long popUpId;
    @Schema(title = "팝업 사진", requiredMode = Schema.RequiredMode.REQUIRED)
    private String popUpPic;
    @Schema(title = "코멘트", requiredMode = Schema.RequiredMode.REQUIRED)
    private String comment;
}