package com.green.acamatch.reports.model;

import com.green.acamatch.entity.reports.ReportsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserListRes {
    @Schema(title = "사용자 이메일")
    private String email;

    @Schema(title = "사용자 이름")
    private String name;

    @Schema(title = "신고당한 이유")
    private ReportsType reportsType;

    @Schema(title = "신고 처리 상태", description = "0: 대기 중 1: 처리 완")
    private int processingStatus;

    @Schema(title = "신고 당한 횟수")
    private int reportCount;

    @Schema(title = "신고 PK")
    private long reportId;
}
