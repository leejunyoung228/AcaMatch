package com.green.acamatch.menuOut;

import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.menuOut.model.MenuOutAcademyAllGetRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Service
@Slf4j
@RestController
public class MenuOutService {
    private AcademyMessage academyMessage;
    private MenuOutMapper menuOutMapper;

    //학원전체조회
    public List<MenuOutAcademyAllGetRes> getAcademyAll() {
        List<MenuOutAcademyAllGetRes> resList = menuOutMapper.getAcademyAll();

        if(resList == null) {
            academyMessage.setMessage("등록된 학원이 없습니다.");
            return resList;
        }
        academyMessage.setMessage("학원전체조회성공");
        return resList;
    }
}
