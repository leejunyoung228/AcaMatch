package com.green.acamatch.attendance.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AttendanceGetDto {
    private long acaId;
    private long classId;
    private String acaName;
    private String className;
    private int present;
    private int late;
    private int absent;
    private int earlyLeave;
    private LocalDate attendanceDate;
    private int sumCount;
}
