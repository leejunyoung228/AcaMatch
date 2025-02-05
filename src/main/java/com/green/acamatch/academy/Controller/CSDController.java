package com.green.acamatch.academy.Controller;

import com.green.acamatch.academy.Service.CSDService;
import com.green.acamatch.academy.model.HB.*;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.config.model.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/academy")
@RequiredArgsConstructor
@Tag(name = "도시 가져오기")
public class CSDController {
    private final CSDService service;
    private final AcademyMessage academyMessage;

    @GetMapping("getCity")
    @Operation(summary = "도시 리스트 불러오기")
    public ResultResponse<List<GetCityRes>> getCityList(){
        List<GetCityRes> res = service.getCityList();
        return ResultResponse.<List<GetCityRes>>builder()
                .resultMessage("도시 리스트 불러오기 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("getStreet")
    @Operation(summary = "시/군/구 리스트 불러오기")
    public ResultResponse<List<GetStreetRes>> getStreetList(GetStreetReq p){
        List<GetStreetRes> res = service.getStreetList(p);
        return ResultResponse.<List<GetStreetRes>>builder()
                .resultMessage("시/군/구 리스트 불러오기 성공")
                .resultData(res)
                .build();
    }

    @GetMapping("getDong")
    @Operation(summary = "동 리스트 불러오기")
    public ResultResponse<List<GetDongRes>> getDongList(GetDongReq p) {
        List<GetDongRes> res = service.getDongList(p);
        return ResultResponse.<List<GetDongRes>>builder()
                .resultMessage("동 리스트 불러오기 성공")
                .resultData(res)
                .build();
    }
}
