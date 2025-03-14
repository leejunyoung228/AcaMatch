package com.green.acamatch.menuOut;

import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.menuOut.model.MenuOutAcademyAllGetRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menuOut")
@Tag(name = "메뉴뎁스 밖으로빼기")
public class MenuOutController {
    private final AcademyMessage academyMessage;
    private final MenuOutService menuOutService;

    @GetMapping("all")
    @Operation(summary = "학원전체조회")
    public ResultResponse<List<MenuOutAcademyAllGetRes>> getAcademyAll() {
        List<MenuOutAcademyAllGetRes> resList = menuOutService.getAcademyAll();
        return ResultResponse.<List<MenuOutAcademyAllGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(resList)
                .build();
    }

}
