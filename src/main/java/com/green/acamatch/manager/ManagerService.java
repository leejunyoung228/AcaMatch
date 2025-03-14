package com.green.acamatch.manager;

import com.green.acamatch.acaClass.AcaClassService;
import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.manager.model.*;
import com.green.acamatch.sms.SmsService;
import com.green.acamatch.sms.model.SmsRequest;
import com.green.acamatch.user.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ManagerService {
    private final SmsService smsService;
    private final AcaClassService acaClassService;
    private final UserUtils userUtils;
    private final ManagerMapper managerMapper;

    // 특정 수업의 학생들에게 출석 알림 문자 발송
    public void sendAttendanceNotificationByClass(Long senderId, Long classId, String message) {
        User sender = userUtils.findUserById(senderId);

        // 관리자 또는 선생님만 문자 전송 가능
        if (!sender.getUserRole().isAcademy() && !sender.getUserRole().isTeacher()) {
            throw new AccessDeniedException("학원 관리자 또는 선생님만 수업 학생들에게 문자를 보낼 수 있습니다.");
        }

        // ✅ classId를 AcaClass 객체로 변환 후 사용
        AcaClass acaClass = acaClassService.findClassById(classId);
        List<User> students = acaClassService.findStudentsByClass(acaClass);

        if (students.isEmpty()) {
            throw new IllegalArgumentException("해당 수업에 등록된 학생이 없습니다.");
        }

        // 각 학생에게 문자 전송
        for (User student : students) {
            SmsRequest smsRequest = new SmsRequest();
            smsRequest.setTo(student.getPhone());  // 수신자 전화번호 설정
            smsRequest.setText(message);           // 문자 메시지 설정
            smsRequest.setClassId(classId);        // 수업 ID 설정

            smsService.sendSms(smsRequest, senderId);  // 올바른 인자 전달
        }
    }

    public List<GetAcademyCountRes> getAcademyCount(String month){
        List<GetAcademyCountRes> res = managerMapper.getAcademyCount(month);
        return res;
    }

    public List<GetUserCountRes> getUserCount(String month){
        List<GetUserCountRes> res = managerMapper.getUserCount(month);
        return res;
    }

    public List<GetAcademyCostCountRes> getAcademyCostCount(String month){
        List<GetAcademyCostCountRes> res = managerMapper.getAcademyCostCount(month);
        return res;
    }

    public List<GetAcademyCostByUserIdRes> getAcademyCostByUserId(GetAcademyCostByUserIdReq req){
        List<GetAcademyCostByUserIdRes> res = managerMapper.getAcademyCostByUserId(req);
        return res;
    }

    public List<GetUserCountByUserIdRes> getUserCountByUserId(GetUserCountByUserIdReq req){
        List<GetUserCountByUserIdRes> res = managerMapper.getUserCountByUserId(req);
        return res;
    }

    public List<GetUserInfoListRes> GetUserInfoList(long userId){
        List<GetUserInfoListRes> res = managerMapper.GetUserInfoList(userId);
        return res;
    }
}
