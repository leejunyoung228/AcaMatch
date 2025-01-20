package com.green.studybridge.academy.Controller;

import com.green.studybridge.academy.Service.TagService;
import com.green.studybridge.academy.model.tag.SelTagRes;
import com.green.studybridge.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;


    @GetMapping("tag")
    @Operation(summary = "태그종류보기")
    public ResultResponse<SelTagRes> selTagList() {
        SelTagRes res = tagService.selTagList();
        return ResultResponse.<SelTagRes>builder()
                .resultMessage("태그들")
                .resultData(res)
                .build();
    }
}
