package com.green.acamatch.grade;

import com.green.acamatch.entity.grade.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("SELECT COUNT(*) FROM Grade a INNER JOIN JoinClass b ON a.joinClass.joinClassId = b.joinClassId" +
            " WHERE b.joinClassId = :joinClassId AND a.exam.examId = :examId")
    Long existsGrade(@Param("joinClassId")Long joinClassId, @Param("examId") Long examId);
}
