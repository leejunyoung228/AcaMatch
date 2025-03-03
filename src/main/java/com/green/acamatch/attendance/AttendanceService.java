package com.green.acamatch.attendance;

import com.green.acamatch.attendance.model.*;
import com.green.acamatch.config.exception.AcaClassErrorCode;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.attendance.Attendance;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.joinClass.JoinClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    @Autowired
    private final AttendanceMapper attendanceMapper;
    private final JoinClassRepository joinClassRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public int postAttendance(AttendancePostReq p) {
        try {
            Attendance attendance = new Attendance();
            JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(()
                    -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
            Long exists = attendanceRepository.existsByJoinClassIdAndAttendanceDateAndStatus
                    (p.getJoinClassId(), p.getAttendanceDate());

            if (exists > 0) {
                throw new CustomException(AcaClassErrorCode.EXISTS_STATUS);
            }

            attendance.setJoinClass(joinClass);
            attendance.setAttendanceDate(p.getAttendanceDate());
            attendance.setCreatedAt(LocalDateTime.now());
            attendance.setStatus(p.getStatus());

            attendanceRepository.save(attendance);

            return 1;
        } catch (IllegalArgumentException e) {
            e.getMessage();
            return 0;
        }
    }

    public List<AttendanceGetDto> getAttendance(AttendanceGetReq p) {
        List<AttendanceGetDto> result = attendanceMapper.getAttendanceStatusCount(p);

        if (result == null || result.isEmpty()) {
            throw new IllegalArgumentException("출석부를 찾을 수 없습니다.");
        }
        return result;
    }

    public List<AttendanceGetUserDto> getAttendanceUser(AttendanceGetUserReq p) {
        List<AttendanceGetUserDto> result = attendanceMapper.getAttendanceUserName(p);

        if (result == null || result.isEmpty()) {
            throw new IllegalArgumentException("수강생을 찾을 수 없습니다.");
        }
        return result;
}

@Transactional
public int updAttendance(AttendancePutReq p) {
    try {
        Attendance attendance = attendanceRepository.findById(p.getAttendanceId()).orElseThrow(()
                -> new CustomException(AcaClassErrorCode.NOT_FOUND_ATTENDANCE));
        JoinClass joinClass = joinClassRepository.findById(p.getJoinClassId()).orElseThrow(()
                -> new CustomException(AcaClassErrorCode.NOT_FOUND_JOIN_CLASS));
        if (p.getStatus() == null) {
            throw new CustomException(AcaClassErrorCode.FAIL_TO_UPD);
        }
        attendance.setJoinClass(joinClass);
        attendance.setAttendanceDate(p.getAttendanceDate());
        attendance.setStatus(p.getStatus());
        attendanceRepository.save(attendance);
        return 1;
    } catch (IllegalArgumentException e) {
        e.getMessage();
        return 0;
    }
}

@Transactional
public int delAttendance(AttendanceDelReq p) {
    try {
        Attendance attendance = attendanceRepository.findById(p.getAttendanceId()).orElseThrow(()
                -> new CustomException(AcaClassErrorCode.NOT_FOUND_ATTENDANCE));
        attendanceRepository.delete(attendance);
        return 1;
    } catch (IllegalArgumentException e) {
        e.getMessage();
        return 0;
    }
}
}