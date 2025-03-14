package com.green.acamatch.menuOut;

import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.model.ResultResponse;
import com.green.acamatch.menuOut.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menuOut")
@Tag(name = "메뉴뎁스 밖으로빼기")
public class MenuOutController {
    private final AcademyMessage academyMessage;
    private final MenuOutService menuOutService;

    @GetMapping("academy")
    @Operation(summary = "학원전체조회")
    public ResultResponse<List<MenuOutAcademyAllGetRes>> getAcademyAll() {
        List<MenuOutAcademyAllGetRes> resList = menuOutService.getAcademyAll();
        return ResultResponse.<List<MenuOutAcademyAllGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(resList)
                .build();
    }

    @GetMapping("class")
    @Operation(summary = "학원당 수업목록 조회")
    public ResultResponse<List<MenuOutAcaClassGetRes>> getAcaClass(MenuOutAcaClassGetReq req) {
        List<MenuOutAcaClassGetRes> resList = menuOutService.getAcaClass(req);
        return ResultResponse.<List<MenuOutAcaClassGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(resList)
                .build();
    }

    @GetMapping("book")
    @Operation(summary = "클래스 내 교재목록 조회")
    public ResultResponse<List<MenuOutAcaClassBookGetRes>> getAcaClassBook(MenuOutAcaClassBookGetReq req) {
        List<MenuOutAcaClassBookGetRes> resList = menuOutService.getAcaClassBook(req);
        return ResultResponse.<List<MenuOutAcaClassBookGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(resList)
                .build();
    }

    @GetMapping("exam")
    @Operation(summary = "학원 -> 강좌 -> 시험 목록")
    public ResultResponse<List<MenuOutExamGetRes>> getExamList(@ModelAttribute @ParameterObject MenuOutExamGetReq p) {
        try {
            List<MenuOutExamGetRes> result = menuOutService.getExamList(p);
            return ResultResponse.<List<MenuOutExamGetRes>>builder()
                    .resultMessage(academyMessage.getMessage())
                    .resultData(result)
                    .build();
        } catch (CustomException e) {
            return ResultResponse.<List<MenuOutExamGetRes>>builder()
                    .resultMessage("시험 목록 불러오기 실패")
                    .resultData(new ArrayList<>())
                    .build();
        }
    }

    @GetMapping("exam/user")
    @Operation(summary = "학원 -> 강좌 -> 시험 -> 수강생 목록")
    public ResultResponse<List<MenuOutExamUserGetRes>> getExamUser(@ModelAttribute @ParameterObject MenuOutExamUserGetReq p) {
        try {
            List<MenuOutExamUserGetRes> result = menuOutService.getExamUser(p);
            return ResultResponse.<List<MenuOutExamUserGetRes>>builder()
                    .resultMessage(academyMessage.getMessage())
                    .resultData(result)
                    .build();
        } catch (CustomException e) {
            return ResultResponse.<List<MenuOutExamUserGetRes>>builder()
                    .resultMessage("수강생 목록 불러오기 실패")
                    .resultData(new ArrayList<>())
                    .build();
        }
    }

}
