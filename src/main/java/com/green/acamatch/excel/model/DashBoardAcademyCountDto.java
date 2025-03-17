package com.green.acamatch.excel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DashBoardAcademyCountDto {
    private LocalDate day;
    private int academyCount;
}