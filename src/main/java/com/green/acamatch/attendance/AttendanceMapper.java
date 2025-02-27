package com.green.acamatch.attendance;

import com.green.acamatch.attendance.model.AttendanceGetDto;
import com.green.acamatch.attendance.model.AttendanceGetReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttendanceMapper {
    List<AttendanceGetDto> getAttendanceStatusCount(AttendanceGetReq req);
}