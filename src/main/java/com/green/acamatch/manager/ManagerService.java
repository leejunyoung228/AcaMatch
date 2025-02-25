package com.green.acamatch.manager;

import com.green.acamatch.entity.user.User;
import com.green.acamatch.sms.SmsService;
import com.green.acamatch.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ManagerService {
    private final UserService userService;
    private final SmsService smsService;

    // ✅ 특정 수업의 학생들에게 출석 알림 문자 발송
    public void sendAttendanceNotificationByClass(Long senderId, Long classId, String message) {
        User sender = userService.findUserById(senderId);

        // ✅ 관리자 또는 선생님만 문자 전송 가능
        if (!sender.getUserRole().isAcademy() && !sender.getUserRole().isTeacher()) {
            throw new AccessDeniedException("학원 관리자 또는 선생님만 수업 학생들에게 문자를 보낼 수 있습니다.");
        }

        List<User> students = userService.findStudentsByClassId(classId);
        if (students.isEmpty()) {
            throw new IllegalArgumentException("해당 수업에 등록된 학생이 없습니다.");
        }

        // ✅ 각 학생에게 문자 전송
        for (User student : students) {
            smsService.sendSms(student.getPhone(), message, senderId, classId); // ✅ sender.getUserId()는 필요 없음
        }
    }
}
