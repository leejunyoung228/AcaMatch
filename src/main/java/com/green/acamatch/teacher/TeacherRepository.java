package com.green.acamatch.teacher;

import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.manager.TeacherIds;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, TeacherIds> {
    Optional<Teacher> findById(TeacherIds teacherIds);

    @Query("SELECT COUNT(*) FROM Academy a INNER JOIN AcaClass b ON a.acaId = b.academy.acaId" +
            " INNER JOIN User c ON a.user.userId = c.userId" +
            " WHERE a.acaId =:acaId AND b.classId = :classId AND c.userId = :userId")
    Long existsByAcademyAndClassAndUser(@Param("acaId") Long acaId, @Param("classId") Long classId, @Param("userId") Long userId);
}