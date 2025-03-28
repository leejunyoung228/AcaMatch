package com.green.acamatch.acaClass;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.user.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<AcaClass, Long> {

//    @Query("SELECT j.user FROM JoinClass j WHERE j.acaClass.classId = :classId")
//    List<User> findStudentsByClassId(@Param("classId") Long classId);
//
//    @Query("SELECT c.teacher FROM AcaClass c WHERE c.classId = :classId")
//    Teacher findTeacherByClassId(@Param("classId") Long classId);
//
//    @Query("SELECT COUNT(*) FROM AcaClass a WHERE a.academy.acaId = :acaId AND a.className = :className")
//    Long existsByAcaIdAndClassName(Long acaId, String className);
//
//
//    @Query("SELECT COUNT(a) FROM AcaClass a WHERE a.academy.acaId = :acaId " +
//            "AND a.className = :className " +
//            "AND a.startDate = :startDate " +
//            "AND a.endDate = :endDate " +
//            "AND (:teacherUserId = 0 OR a.teacher.user.userId = :teacherUserId)")
//    Long countByAcaIdAndClassNameAndStartDateAndEndDateAndTeacherUserId(
//            @Param("acaId") Long acaId,
//            @Param("className") String className,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("teacherUserId") Long teacherUserId);

    Optional<AcaClass> findById(Long classId);

    List<AcaClass> findByAcademy_AcaId(Long acaId);

    @Query("SELECT ac.academy.acaId FROM AcaClass ac WHERE ac.classId = :classId")
    Optional<Long> findAcaIdByClassId(@Param("classId") Long classId);

    // ✅ 특정 학급(classId)에 속한 학생들 조회
    @Query("SELECT j.user FROM JoinClass j WHERE j.acaClass.classId = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);


}