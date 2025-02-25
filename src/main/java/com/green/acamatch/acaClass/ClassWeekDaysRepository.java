package com.green.acamatch.acaClass;

import com.green.acamatch.entity.acaClass.ClassWeekdays;
import com.green.acamatch.entity.acaClass.ClassWeekdaysIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassWeekDaysRepository extends JpaRepository<ClassWeekdays, ClassWeekdaysIds> {

    @Query("SELECT COUNT(*) FROM ClassWeekdays a WHERE a.classWeekdaysIds.dayId = :dayId AND a.classId = :classId")
    boolean existsClassWeekDays(Long dayId, Long classId);
}