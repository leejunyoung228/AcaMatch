package com.green.acamatch.popUp;

import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.popUp.model.PopUpPostReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Operation(summary = "팝업 등록")
    public ResultResponse<Integer> postPopUp(@RequestPart(required = false) MultipartFile pic, @Valid @RequestPart PopUpPostReq p) {
        log.info("p, pic: {}, {}", p, pic);
        int result = popUpService.PostPopUp(pic,p);
        return ResultResponse.<Integer>builder()
                .resultMessage("팝업 등록이 성공하였습니다.")
                .resultData(result)
                .build();
    }
}