package com.green.acamatch.academy.mapper;

import com.green.acamatch.academy.model.HB.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CSDMapper {
    // 도시 리스트 가져오기
    List<GetCityRes> getCity();

    // 시/군/구 리스트 가져오기
    List<GetStreetRes> getStreet(GetStreetReq p);

    // 동 리스트 가져오기
    List<GetDongRes> getDong(GetDongReq p);
}
