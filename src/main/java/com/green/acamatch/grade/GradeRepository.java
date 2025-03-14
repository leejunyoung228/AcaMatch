package com.green.acamatch.grade;

import com.green.acamatch.entity.grade.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("SELECT COUNT(*) FROM Grade a INNER JOIN JoinClass b ON a.joinClass.joinClassId = b.joinClassId" +
            " WHERE b.joinClassId = :joinClassId AND a.exam.examId = :examId")
    Long existsGrade(@Param("joinClassId")Long joinClassId, @Param("examId") Long examId);


    @Query("SELECT B.joinClassId, A.name, D.examId, D.examName, E.examDate, E.gradeId, D.examType," +
            "CASE WHEN D.examType = 0 THEN E.score ELSE NULL END AS score," +
            "CASE WHEN D.examType != 0 THEN " +
            "CASE WHEN COALESCE(E.pass,0) = 0 THEN 0 ELSE 1 END ELSE NULL END AS pass\n, COALESCE(E.processingStatus,0) AS processing_status\n" +
            "FROM User A\n" +
            "INNER JOIN JoinClass B\n" +
            "ON A.userId = B.user.userId\n" +
            "INNER JOIN AcaClass C\n" +
            "ON B.acaClass.classId = C.classId \n" +
            "INNER JOIN Exam D\n" +
            "ON C.classId = D.classId.classId\n" +
            "LEFT JOIN Grade E\n" +
            "ON D.examId = E.exam.examId\n" +
            "AND B.joinClassId = E.joinClass.joinClassId\n" +
            "WHERE D.examId = :examId\n" +
            "GROUP BY B.joinClassId, A.name, D.examId, D.examName, E.examDate, E.gradeId, D.examType, E.processingStatus\n" +
            "ORDER BY B.joinClassId\n")
    List<Object[]> findExamGradeByExamId(@Param("examId")Long examId);
}