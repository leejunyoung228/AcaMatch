package com.green.acamatch.popUp.model;

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
}