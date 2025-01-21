package com.green.studybridge.academy.Controller;

import com.green.studybridge.academy.Service.TagService;
import com.green.studybridge.academy.model.UserMessage;
import com.green.studybridge.academy.tag.SelTagReq;
import com.green.studybridge.academy.tag.SelTagRes;
import com.green.studybridge.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final UserMessage userMessage;


    @GetMapping
    @Operation(summary = "태그종류보기")
    public ResultResponse<SelTagRes> selTagList(@ModelAttribute SelTagReq req) {
        SelTagRes res = tagService.selTagList(req);
        return ResultResponse.<SelTagRes>builder()
                .resultMessage(userMessage.getMessage())
                .resultData(res)
                .build();
    }
}
