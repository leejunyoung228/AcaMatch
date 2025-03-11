package com.green.acamatch.excel.model;

import com.green.acamatch.academy.model.HB.GetAcademyCountRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DashBoardDto {
    private LocalDate date; // 날짜
    private int academyCount; // 학원 수
    private int userCount; // 사용자 수
    private int paymentCount; // 결제 내역
}


