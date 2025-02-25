package com.green.acamatch.acaClass;

import com.green.acamatch.entity.acaClass.Weekdays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekDaysRepository extends JpaRepository<Weekdays, Long> {

    @Query("SELECT COUNT(*) FROM Weekdays a WHERE a.day = :day")
    boolean existsDay(String day);
}