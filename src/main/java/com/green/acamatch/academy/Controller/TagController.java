package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.TagService;
import com.green.acamatch.academy.model.AcademyMessage;
import com.green.acamatch.academy.tag.SelTagReq;
import com.green.acamatch.academy.tag.SelTagRes;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("tag")
@RequiredArgsConstructor
@Tag(name = "태그")
public class TagController {
    private final TagService tagService;
    private final AcademyMessage academyMessage;


    @GetMapping
    @Operation(summary = "태그종류보기", description = "입력한 글자를 가진 태그를 검색 할 수 있습니다.(한 글자라도 가지고 있으면 검색가능)")
    public ResultResponse<SelTagRes> selTagList(@ModelAttribute SelTagReq req) {
        SelTagRes res = tagService.selTagList(req);
        return ResultResponse.<SelTagRes>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }
}
