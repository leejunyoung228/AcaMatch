package com.green.acamatch.exam;

import com.green.acamatch.entity.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT COUNT(*) FROM Exam a WHERE a.classId.classId = :classId AND a.examName = :examName")
    Long existsExam(@Param("examId") Long examId, @Param("examName") String examName);
}