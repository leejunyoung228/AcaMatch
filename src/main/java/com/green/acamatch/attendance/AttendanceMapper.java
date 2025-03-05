package com.green.acamatch.attendance;

import com.green.acamatch.attendance.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttendanceMapper {
    List<AttendanceGetDto> getAttendanceStatusCount(AttendanceGetReq p);
    List<AttendanceGetUserDto> getAttendanceUserName(AttendanceGetUserReq p);
    List<AcademyAttendanceGetRes> getAcademyAttendanceStatusCount(AcademyAttendanceGetReq p);
}