package com.green.acamatch.ai;

import com.green.acamatch.ai.model.GetFeedBackRes;
import com.green.acamatch.ai.model.PostFeedBackReq;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("ai")
@RestController
@RequiredArgsConstructor
@Tag(name = "AI")
public class AiController {
    private final AiService service;

    @PostMapping("postFeedBack")
    @Operation(summary = "피드백 등록", description = "gradeId와 feedBack을 입력받아 데이터베이스에 등록됩니다.")
    public ResultResponse<Integer> postFeedBack(@RequestBody PostFeedBackReq p){
        int result = service.postFeedBack(p);
        return ResultResponse.<Integer>builder()
                .resultMessage(p.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping("{gradeId}")
    @Operation(summary = "피드백 불러오기", description = "gradeId를 보내주시면 최근 3개의 피드백을 불러옵니다.")
    public ResultResponse<List<GetFeedBackRes>> getFeedBack(@PathVariable @ModelAttribute Integer gradeId){
        List<GetFeedBackRes> result = service.getFeedBack(gradeId);
        return ResultResponse.<List<GetFeedBackRes>>builder()
                .resultMessage(result == null ? "피드백이 없습니다." : "피드백 출력 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("getApiKey")
    @Operation(summary = "api키 가져오기")
    public ResultResponse<String> getApiKey(){
        String result = service.getApiKey();
        return ResultResponse.<String>builder()
                .resultMessage("출력 성공")
                .resultData(result)
                .build();
    }
}
