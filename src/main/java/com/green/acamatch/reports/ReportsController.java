package com.green.acamatch.reports;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.reports.model.PostReportsReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reports")
@RequiredArgsConstructor
@Tag(name = "신고")
public class ReportsController {
    private final ReportsService reportsService;

    @PostMapping("postReports")
    @Operation(summary = "신고 등록", description = "리뷰, 채팅에서 신고했으면 reportedUser만" +
            "학원을 신고했으면 acaId만 넣으시면 됩니다.")
    public ResultResponse<Integer> postReports(@ParameterObject PostReportsReq req){
        int result = reportsService.postReports(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("등록 성공")
                .resultData(result)
                .build();
    }
}
