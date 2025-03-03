package com.green.acamatch.attendance;

import com.green.acamatch.entity.attendance.Attendance;
import com.green.acamatch.entity.attendance.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT COUNT(*) FROM Attendance a " +
            "WHERE a.joinClass.joinClassId = :joinClassId AND a.attendanceDate = :attendanceDate")
    Long existsByJoinClassIdAndAttendanceDateAndStatus(@Param("joinClassId") Long joinClassId,
                                                       @Param("attendanceDate") LocalDate attendanceDate);
}