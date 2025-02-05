package com.green.acamatch.academy.Service;

import com.green.acamatch.academy.mapper.CSDMapper;
import com.green.acamatch.academy.model.HB.*;
import com.green.acamatch.academy.model.JW.AcademyMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CSDService {
    private final CSDMapper mapper;
    private AcademyMessage academyMessage;

    // 도시 리스트 가져오기
    public List<GetCityRes> getCityList() {return mapper.getCity();}

    // 시/군/구 리스트 가져오기
    public List<GetStreetRes> getStreetList(GetStreetReq p) {
        List<GetStreetRes> list = mapper.getStreet(p);
        if(list == null){
            academyMessage.setMessage("시/군/구 리스트를 불러오기 실패");
            return null;
        }
        academyMessage.setMessage("시/군/구 리스트를 불러오기 성공");
        return mapper.getStreet(p);
    }

    // 동 리스트 가져오기
    public List<GetDongRes> getDongList(GetDongReq p) {
        List<GetDongRes> list = mapper.getDong(p);
        if(list.size() == 0){
            academyMessage.setMessage("동 리스트 불러오기 실패");
            return null;
        }
        academyMessage.setMessage("동 리스트 불러오기 성공");
        return list;
    }
}
