package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.AcademyService;
import com.green.acamatch.academy.Service.TagService;
import com.green.acamatch.academy.model.*;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/academy")
@RequiredArgsConstructor
@Tag(name = "학원")
public class AcademyController {
    private final AcademyService academyService;
    private final TagService tagService;
    private final AcademyMessage academyMessage;


    @PostMapping
    @Operation(summary = "학원정보등록", description = "필수: 유저 PK, 동 PK, 학원 이름, 학원 번호, 학원 상세 주소 || 옵션: 학원 설명, 강사 수, 오픈 시간, 마감 시간, 학원 사진, 태그")
    public ResultResponse<Integer> postAcademy(@RequestPart(required = false) MultipartFile pic, @Valid @RequestPart AcademyPostReq req) {
        int result1 = academyService.insAcademy(pic, req);
        int result2 = tagService.insAcaTag(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(result1)
                .build();
    }

    @PutMapping
    @Operation(summary = "학원정보수정", description = "acaId, userId는 필수로 받고, 수정하기 원하는 항목 값을 입력합니다.")
    public ResultResponse<Integer> putAcademy(@RequestPart(required = false) MultipartFile pic, @RequestPart AcademyUpdateReq req) {
        int result = academyService.updAcademy(pic, req);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(result)
                .build();
    }

    @DeleteMapping
    @Operation(summary = "학원정보삭제", description = "acaId, userId 필수로 받아야 삭제가 가능합니다.")
    public ResultResponse<Integer> deleteAcademy(@ModelAttribute AcademyDeleteReq req) {
        int result = academyService.delAcademy(req);
        return ResultResponse.<Integer>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(result)
                .build();
    }

    @GetMapping("best")
    @Operation(summary = "학원들의 좋아요 순", description = "메인페이지는 page=1, size=4, 다른페이지는 맞게 값 요청해주세요.")
    public ResultResponse<List<AcademyBestLikeGetRes>> getAcademyBest(@ModelAttribute AcademySelOrderByLikeReq req) {
        List<AcademyBestLikeGetRes> list = academyService.getAcademyBest(req);
        return ResultResponse.<List<AcademyBestLikeGetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(list)
                .build();
    }

// -------------------------------------------------------------

    @GetMapping("academyList")
    @Operation(summary = "학원 리스트 검색", description = "startIdx, size 값은 지우고 해보시면 됩니다, 완성이라고 생각했는데, 생각했던거랑 다르게 돌아가야 할것 같아서 아직 미완성입니다. ㅠㅠ")
    public ResultResponse<List<GetAcademyRes>> getAcademyList(@ParameterObject @ModelAttribute GetAcademyReq p){
        List<GetAcademyRes> res = academyService.getAcademyRes(p);
        return ResultResponse.<List<GetAcademyRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }

    @GetMapping("academyDetail/{acaId}")
    @Operation(summary = "학원 정보 자세히 보기")
    public ResultResponse<GetAcademyDetail> getAcademyDetail(@PathVariable Long acaId){
        GetAcademyDetail res = academyService.getAcademyDetail(acaId);
        log.info("result: {}", res);
        return ResultResponse.<GetAcademyDetail>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }

    @GetMapping("tagList/{acaId}")
    @Operation(summary = "등록된 태그 불러오기")
    public ResultResponse<List<GetAcademyTagDto>> getTagList(@PathVariable @ModelAttribute Long acaId){
        List<GetAcademyTagDto> list = academyService.getTagList(acaId);
        return ResultResponse.<List<GetAcademyTagDto>>builder()
                .resultMessage("태그 불러오기")
                .resultData(list)
                .build();
    }

    @GetMapping("getCity")
    @Operation(summary = "도시 리스트 불러오기")
    public ResultResponse<List<GetCityRes>> getCityList(){
        List<GetCityRes> res = academyService.getCityList();
        return ResultResponse.<List<GetCityRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }

    @GetMapping("getStreet")
    @Operation(summary = "시/군/구 리스트 불러오기")
    public ResultResponse<List<GetStreetRes>> getStreetList(GetStreetReq p){
        List<GetStreetRes> res = academyService.getStreetList(p);
        return ResultResponse.<List<GetStreetRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }

    @GetMapping("getDong")
    @Operation(summary = "동 리스트 불러오기")
    public ResultResponse<List<GetDongRes>> getDongList(GetDongReq p) {
        List<GetDongRes> res = academyService.getDongList(p);
        return ResultResponse.<List<GetDongRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(res)
                .build();
    }

    @GetMapping("getAcademyListByDong")
    @Operation(summary = "동만 입력받아 학원 리스트 불러오기")
    public ResultResponse<List<GetAcademyByDongRes>> getAcademyByDongList(@ParameterObject @ModelAttribute GetAcademyByDongReq p){
        List<GetAcademyByDongRes> list = academyService.getAcademyByDongResList(p);
        return ResultResponse.<List<GetAcademyByDongRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(list)
                .build();
    }

    @GetMapping("getAcademyListBySearchName")
    @Operation(summary = "동과 검색어를 입력받아 학원 리스트 불러오기")
    public ResultResponse<List<GetAcademyBySearchNameRes>> getAcademyListBySearchName(@ParameterObject @ModelAttribute GetAcademyBySearchNameReq p){
        List<GetAcademyBySearchNameRes> list = academyService.getAcademyListBySearchName(p);
        return ResultResponse.<List<GetAcademyBySearchNameRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(list)
                .build();
    }

    @GetMapping("getAcademyListByOnlySearName")
    @Operation(summary = "검색어만 입력받아 학원 리스트 불러오기")
    public ResultResponse<List<GetAcademyByOnlySearchNameRes>> getAcademyByOnlySearchName(@ParameterObject @ModelAttribute GetAcademyByOnlySearchNameReq p){
        List<GetAcademyByOnlySearchNameRes> list = academyService.getAcademyByOnlySearchName(p);
        return ResultResponse.<List<GetAcademyByOnlySearchNameRes>>builder()
                .resultMessage(academyMessage.getMessage())
                .resultData(list)
                .build();
    }
}
