package com.green.acamatch.popUp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class PopUpPutReq {
    @Schema(title = "팝업 PK", requiredMode = Schema.RequiredMode.REQUIRED)
    private long popUpId;
    @Schema(title = "팝업 제목", example = "아카매치 GRAND OPEN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;
    @Schema(title = "팝업 내용", example = "아카매치가 개업했습니다.")
    private String comment;
    @Schema(title = "시작 날짜", example = "2025-02-21")
    private LocalDate startDate;
    @Schema(title = "종료 날짜", example = "2025-02-22")
    private LocalDate endDate;
    @JsonIgnore
    private String popUpPic;
    @Schema(title = "활성/비활성", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int popUpShow;
    @Schema(title = "대상",example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private int popUpType;
}
