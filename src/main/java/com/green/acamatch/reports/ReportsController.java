package com.green.acamatch.reports;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.entity.reports.ActionType;
import com.green.acamatch.entity.reports.ReportsType;
import com.green.acamatch.reports.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("reportsTypes")
    public List<Map<String, String>> getClassTypes() {
        return Arrays.stream(ReportsType.values())
                .map(type -> Map.of(
                        "name", type.name(),
                        "description", type.getDescription()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("getUserList")
    @Operation(summary = "신고 당한 유저 리스트 불러오기", description = "updatedAt이 조치한 날짜, exposureEndDate는 조치가 끝나는 날짜입니다.")
    public ResultResponse<List<GetUserListRes>> getUserList(@ParameterObject GetUserListReq req){
        List<GetUserListRes> userList = reportsService.getUserList(req);
        return ResultResponse.<List<GetUserListRes>>builder()
                .resultMessage("출력 성공")
                .resultData(userList)
                .build();
    }

    @GetMapping("getAcademyList")
    @Operation(summary = "신고 당한 학원 리스트 불러오기")
    public ResultResponse<List<GetAcademyListRes>> getAcademyList(@ParameterObject GetAcademyListReq req){
        List<GetAcademyListRes> result = reportsService.getAcademyList(req);
        return ResultResponse.<List<GetAcademyListRes>>builder()
                .resultMessage("조회 성공")
                .resultData(result)
                .build();
    }

    @GetMapping("getReviewList")
    @Operation(summary = "신고 당한 리뷰 조회")
    public ResultResponse<List<GetReviewListRes>> getReviewList(@ParameterObject GetReviewListReq req){
        List<GetReviewListRes> result = reportsService.getReviewList(req);
        return ResultResponse.<List<GetReviewListRes>>builder()
                .resultMessage("조회 성공")
                .resultData(result)
                .build();
    }
}
