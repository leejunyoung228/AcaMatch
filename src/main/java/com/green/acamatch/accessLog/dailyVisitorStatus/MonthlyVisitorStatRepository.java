package com.green.acamatch.accessLog.dailyVisitorStatus;

import com.green.acamatch.entity.dailyVisitorStatus.MonthlyVisitorStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyVisitorStatRepository extends JpaRepository<MonthlyVisitorStat, Long> {

    // 특정 월(month)의 방문자 통계를 조회
    Optional<MonthlyVisitorStat> findFirstByMonthOrderByIdDesc(YearMonth month);


}
