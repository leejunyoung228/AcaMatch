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

    // 특정 수업의 학생들에게 등하원 문자 발송
    public void sendAttendanceNotificationByClass(Long senderId, Long classId, String message) {
        User sender = userService.findUserById(senderId);

        if (!sender.getUserRole().isAcademy() && !sender.getUserRole().isTeacher()) {
            throw new AccessDeniedException("학원 관리자 또는 선생님만 수업 학생들에게 문자를 보낼 수 있습니다.");
        }

        List<User> students = userService.findStudentsByClassId(classId);
        if (students.isEmpty()) {
            throw new IllegalArgumentException("해당 수업에 등록된 학생이 없습니다.");
        }

        for (User student : students) {
            smsService.sendSms(student.getPhone(), message);
        }
    }
}
