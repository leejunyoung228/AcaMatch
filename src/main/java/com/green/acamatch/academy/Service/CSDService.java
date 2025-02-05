package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.mapper.CSDMapper;
import com.green.acamatch.academy.model.HB.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CSDService {
    private final CSDMapper mapper;

    // 도시 리스트 가져오기
    public List<GetCityRes> getCityList() {return mapper.getCity();}

    // 시/군/구 리스트 가져오기
    public List<GetStreetRes> getStreetList(GetStreetReq p) {
        List<GetStreetRes> list = mapper.getStreet(p);
        return list;
    }

    // 동 리스트 가져오기
    public List<GetDongRes> getDongList(GetDongReq p) {
        List<GetDongRes> list = mapper.getDong(p);
        return list;
    }
}
