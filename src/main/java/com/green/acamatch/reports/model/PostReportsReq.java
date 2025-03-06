package com.green.acamatch.reports.model;

import com.green.acamatch.entity.reports.ReportsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReportsReq {
    @Schema(title = "신고한 유저 PK")
    private long reporter;

    @Schema(title = "신고 당한 사람 유저 PK", description = "리뷰, 채팅쪽에서 신고하면 이걸로 입력하시면 됩니다.")
    private Long reportedUser;

    @Schema(title = "신고 당한 학원 PK", description = "학원이 신고 당했다면 이걸로 하시면 됩니다.")
    private Long acaId;

    @Schema(title = "신고 사유")
    private ReportsType reportsType;
}
