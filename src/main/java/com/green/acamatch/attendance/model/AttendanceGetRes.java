package com.green.acamatch.attendance.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AttendanceGetRes {
    List<AttendanceGetDto> attendanceGetDtoList;
}