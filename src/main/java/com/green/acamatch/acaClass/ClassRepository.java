package com.green.acamatch.acaClass;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<AcaClass, Long> {

    @Query("SELECT j.user FROM JoinClass j WHERE j.classId.classId = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);

    @Query("SELECT c.teacher FROM AcaClass c WHERE c.classId = :classId")
    Teacher findTeacherByClassId(@Param("classId") Long classId);

    @Query("SELECT COUNT(*) FROM AcaClass a WHERE a.academy.acaId = :acaId AND a.className = :className")
    Long existsByAcaIdAndClassName(Long acaId, String className);
}