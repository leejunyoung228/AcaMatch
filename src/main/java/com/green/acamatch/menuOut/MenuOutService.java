package com.green.acamatch.menuOut;

import com.green.acamatch.academy.model.JW.AcademyMessage;
import com.green.acamatch.menuOut.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuOutService {
    private final AcademyMessage academyMessage;
    private final MenuOutMapper menuOutMapper;

    //학원전체조회
    public List<MenuOutAcademyAllGetRes> getAcademyAll() {
        List<MenuOutAcademyAllGetRes> resList = menuOutMapper.getAcademyAll();

        if(resList == null) {
            academyMessage.setMessage("등록된 학원이 없습니다.");
            return resList;
        }
        academyMessage.setMessage("학원 전체 조회성공");
        return resList;
    }

    //학원당 클래스목록 조회
    public List<MenuOutAcaClassGetRes> getAcaClass(MenuOutAcaClassGetReq req) {
        List<MenuOutAcaClassGetRes> resList = menuOutMapper.getAcaClass(req.getAcaId());

        if(resList == null) {
            academyMessage.setMessage("등록된 수업이 없습니다.");
            return resList;
        }
        academyMessage.setMessage("학원당 수업목록 조회 성공");
        return resList;
    }

    //학원클래스 내 교재목록 조회
    public List<MenuOutAcaClassBookGetRes> getAcaClassBook(MenuOutAcaClassBookGetReq req) {
        List<MenuOutAcaClassBookGetRes> resList = menuOutMapper.getAcaClassBook(req.getAcaId(), req.getClassId());

        if(resList == null) {
            academyMessage.setMessage("등록된 교재가 없습니다.");
            return resList;
        }
        academyMessage.setMessage("교재 목록 조회 성공");
        return resList;
    }

    public List<MenuOutExamGetRes> getExamList(MenuOutExamGetReq p) {
        List<MenuOutExamGetRes> result = menuOutMapper.getExamList(p);

        if(result == null || result.isEmpty()) {
            academyMessage.setMessage("등록된 시험이 없습니다.");
            return result;
        }
        academyMessage.setMessage("시험 조회 성공");
        return result;
    }

    //학원 -> 강좌 -> 시험 -> 수강생 조회
    public List<MenuOutExamUserGetRes> getExamUser(MenuOutExamUserGetReq p) {
        List<MenuOutExamUserGetRes> result = menuOutMapper.getExamUser(p);

        if(result == null || result.isEmpty()) {
            academyMessage.setMessage("수강생 조회 실패");
            return result;
        }
        academyMessage.setMessage("수강생 조회 성공");
        return result;
    }
}
