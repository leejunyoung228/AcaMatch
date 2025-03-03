package com.green.acamatch.popUp;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.popUp.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "팝업 관리", description = "팝업 등록, 가져오기, 수정, 삭제")
@RestController
@RequestMapping("/popUp")
@RequiredArgsConstructor
@Slf4j
public class PopUpController {
    private final PopUpService popUpService;

    @PostMapping
    @Operation(summary = "팝업 등록",
            description = "postMan으로 테스트/ pic을 넣을 때는 comment 부분 완전히 지우셔야합니다." +
                    "/ 0 : 일반 사용자 대상, 1: 사이트 관리자가 학원 관리자들에게 메세지보내는 것")
    public ResultResponse<Integer> postPopUp(@RequestPart(required = false) MultipartFile pic, @Valid @RequestPart PopUpPostReq p) {
        log.info("p, pic: {}, {}", p, pic);
        int result = popUpService.PostPopUp(pic,p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "팝업 등록 성공" : "팝업 등록 실패")
                .resultData(result)
                .build();
    }

    @GetMapping
    @Operation(summary = "팝업 리스트 가져오기")
    public ResultResponse<List<PopUpGetDto>> getPopUpList(@ParameterObject @ModelAttribute PopUpGetReq p) {
        List<PopUpGetDto> result = popUpService.getPopUpList(p);
        return ResultResponse.<List<PopUpGetDto>>builder()
                .resultMessage("팝업 리스트 보기 완료")
                .resultData(result)
                .build();
    }

    @GetMapping("detail")
    @Operation(summary = "팝업 상세정보 가져오기")
    public ResultResponse<List<PopUpGetDto>> getPopUpDetail(@ParameterObject @ModelAttribute PopUpGetDetailReq p) {
        List<PopUpGetDto> result = popUpService.getPopUpDetail(p);
        return ResultResponse.<List<PopUpGetDto>>builder()
                .resultMessage("팝업 상세정보 보기 완료")
                .resultData(result)
                .build();
    }

    @PutMapping
    @Operation(summary = "팝업 수정", description = "postMan으로 테스트")
    public ResultResponse<Integer> updPopUp(@RequestPart(required = false) MultipartFile pic, @Valid @RequestPart PopUpPutReq p) {
        int result = popUpService.UpdPopUp(pic, p);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "팝업 수정 성공" : "팝업 수정 실패")
                .resultData(result)
                .build();
    }

    @DeleteMapping("{popUpId}")
    @Operation(summary = "팝업 삭제")
    public ResultResponse<Integer> delPopUp(@PathVariable @ModelAttribute Long popUpId) {
        int result = popUpService.delPopUp(popUpId);
        return ResultResponse.<Integer>builder()
                .resultMessage(result == 1 ? "팝업 삭제 성공" : "팝업 삭제 실패")
                .resultData(result)
                .build();
    }
}