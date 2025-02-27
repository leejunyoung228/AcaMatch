package com.green.acamatch.attendance.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AttendanceGetReq {
    private long acaId;
    private long classId;
    private LocalDate startDate;
    private LocalDate endDate;
}