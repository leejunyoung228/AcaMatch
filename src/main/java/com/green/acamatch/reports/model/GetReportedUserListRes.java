package com.green.acamatch.reports.model;

import com.green.acamatch.entity.reports.ReportsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReportedUserListRes {
    @Schema(title = "유저 닉네임")
    private String name;

    @Schema(title = "신고 유형")
    private ReportsType reportsType;

    @Schema(title = "신고 횟수")
    private int reportCount;

    @Schema(title = "처리 상태", description = "0이면 처리 대기, 1이면 처리 완료, 2이면 무죄 판결")
    private int processingStatus;
}
