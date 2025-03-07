package com.green.acamatch.reports;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.entity.reports.ActionType;
import com.green.acamatch.reports.model.PostReportsReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("updateReports")
    @Operation(summary = "신고 조치")
    public ResultResponse<Integer> updateReports(@ParameterObject long reportsId, ActionType actionType){
        int result = reportsService.updateReports(reportsId, actionType);
        return ResultResponse.<Integer>builder()
                .resultMessage("조치 완료")
                .resultData(result)
                .build();
    }

    @DeleteMapping("deleteReports/{reportsId}")
    @Operation(summary = "신고 취소")
    public ResultResponse<Integer> deleteReports(@PathVariable long reportsId){
        int result = reportsService.deleteReports(reportsId);
        return ResultResponse.<Integer>builder()
                .resultMessage("취소 완료")
                .resultData(result)
                .build();
    }
}
