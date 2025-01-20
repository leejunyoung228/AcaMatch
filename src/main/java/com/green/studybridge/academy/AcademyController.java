package com.green.studybridge.academy;

import com.green.studybridge.academy.model.*;
import com.green.studybridge.academy.model.category.CategoryGetAgeRangeRes;
import com.green.studybridge.academy.model.category.CategoryGetDaysRes;
import com.green.studybridge.academy.model.category.CategoryGetLevelRes;
import com.green.studybridge.academy.model.tag.SelTagRes;
import com.green.studybridge.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("academy")
@RequiredArgsConstructor
@Tag(name = "학원")
public class AcademyController {
    private final AcademyService academyService;

    @GetMapping("tag")
    @Operation(summary = "태그종류보기")
    public ResultResponse<SelTagRes> selTagList() {
        SelTagRes res = academyService.selTagList();
        return ResultResponse.<SelTagRes>builder()
                .resultMessage("태그들")
                .resultData(res)
                .build();
    }

    @GetMapping("age")
    @Operation(summary = "수강연령대 select")
    public ResultResponse<List<CategoryGetAgeRangeRes>> selCategoryAgeRangeList() {
        List<CategoryGetAgeRangeRes> list = academyService.categoryAgeRangeResList();
        return ResultResponse.<List<CategoryGetAgeRangeRes>>builder()
                .resultMessage("수강연령대 select 성공")
                .resultData(list)
                .build();
    }

    @GetMapping("level")
    @Operation(summary = "수준 select")
    public ResultResponse<List<CategoryGetLevelRes>> selCategoryLevelList() {
        List<CategoryGetLevelRes> list = academyService.categoryLevelResList();
        return ResultResponse.<List<CategoryGetLevelRes>>builder()
                .resultMessage("요일 select 성공")
                .resultData(list)
                .build();
    }

    @GetMapping("days")
    @Operation(summary = "요일 select")
    public ResultResponse<List<CategoryGetDaysRes>> selCategoryDaysList() {
        List<CategoryGetDaysRes> list = academyService.categoryDaysResList();
        return ResultResponse.<List<CategoryGetDaysRes>>builder()
                .resultMessage("요일 select 성공")
                .resultData(list)
                .build();
    }

    @PostMapping
    @Operation(summary = "학원정보등록")
    public ResultResponse<Integer> postAcademy(@RequestPart(required = false) MultipartFile pic, @RequestPart AcademyPostReq req) {
        academyService.insAcademy(pic, req);
        academyService.insAcaAgeRange(req);
        academyService.insAcaLevel(req);
        academyService.insAcaDays(req);
        academyService.insAcaTag(req);
        return ResultResponse.<Integer>builder()
                .resultMessage("학원정보등록성공")
                .resultData(1)
                .build();
    }

    @PutMapping
    @Operation(summary = "학원정보수정")
    public int putAcademy(@RequestPart(required = false) MultipartFile pic, @RequestPart AcademyUpdateReq req) {
        academyService.updAcademy(pic, req);
        return 1;
    }

    @DeleteMapping
    @Operation(summary = "학원정보삭제")
    public int deleteAcademy(@ModelAttribute AcademyDeleteReq req) {
        academyService.delAcademy(req);
        return 1;
    }

// -------------------------------------------------------------

    /*@PostMapping()
    @Operation(summary = "학원 등록", description = "필수: 유저 PK, 동 PK, 학원 이름, 학원 번호, 학원 상세 주소 || 옵션: 학원 설명, 강사 수, 오픈 시간, 마감 시간, 학원 사진")
    public ResultResponse<Integer> academyPost(@RequestPart postAcademy p,
                                               @RequestPart MultipartFile pic) {
        int result = service.postAca(p, pic);
        log.info("result: {}", result);
        return ResultResponse.<Integer>builder()
                .resultMessage("학원 등록 성공")
                .resultData(result)
                .build();
    }*/

    @GetMapping("academyList")
    @Operation(summary = "학원 리스트 검색")
    public ResultResponse<List<getAcademyRes>> getAcademyList(@ParameterObject @ModelAttribute getAcademyReq p){
        List<getAcademyRes> res = academyService.getAcademyRes(p);
        return ResultResponse.<List<getAcademyRes>>builder()
                .resultMessage("학원리스트 검색 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("academyDetail/{acaId}")
    @Operation(summary = "학원 정보 자세히 보기")
    public ResultResponse<GetAcademyDetail> getAcademyDetail(@PathVariable Long acaId){
        GetAcademyDetail res = academyService.getAcademyDetail(acaId);
        log.info("result: {}", res);
        return ResultResponse.<GetAcademyDetail>builder()
                .resultMessage("학원 상세보기 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("tagList")
    @Operation(summary = "등록된 태그 불러오기")
    public ResultResponse<List<GetTagList>> getTagList(){
        List<GetTagList> list = academyService.getTagList();
        return ResultResponse.<List<GetTagList>>builder()
                .resultMessage("태그 불러오기")
                .resultData(list)
                .build();
    }

}
