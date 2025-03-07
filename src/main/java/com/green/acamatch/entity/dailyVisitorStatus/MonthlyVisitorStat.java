package com.green.acamatch.entity.dailyVisitorStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

import java.time.YearMonth;


@Entity
@Table(name = "monthly_visitor_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyVisitorStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = YearMonthAttributeConverter.class) // YearMonth를 문자열로 변환
    @Column(name = "month", nullable = false, length = 7) // 'YYYY-MM' 형식 사용
    private YearMonth month;

    @Column(name = "total_visits", nullable = false)
    private Long totalVisits;
}
