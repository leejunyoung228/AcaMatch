package com.green.acamatch.acaClass;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.acaClass.ClassWeekdays;
import com.green.acamatch.entity.acaClass.ClassWeekdaysIds;
import org.apache.poi.ss.formula.functions.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassWeekDaysRepository extends JpaRepository<ClassWeekdays, ClassWeekdaysIds> {

    @Query("SELECT COUNT(*) FROM ClassWeekdays a WHERE a.day.dayId = :dayId AND a.classId.classId = :classId")
    Long existsClassWeekDays(@Param("dayId") Long dayId, @Param("classId") Long classId);
}