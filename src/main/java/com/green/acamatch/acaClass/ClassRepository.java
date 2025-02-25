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
public interface ClassRepository extends JpaRepository<AcaClass, Long> {  // ✅ AcaClass로 변경

    // 특정 수업에 등록된 학생 리스트 조회
    @Query("SELECT j.user FROM JoinClass j WHERE j.classId.classId = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);

    // 특정 수업을 담당하는 선생님 조회
    @Query("SELECT c.teacher FROM AcaClass c WHERE c.classId = :classId")
    Teacher findTeacherByClassId(@Param("classId") Long classId);
}
