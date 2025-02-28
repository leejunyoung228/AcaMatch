package com.green.acamatch.teacher;

import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.manager.TeacherIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, TeacherIds> {
    Optional<Teacher> findById(TeacherIds teacherIds);

    @Query("SELECT a.email FROM User a WHERE a.userId = :userId")
    String findEmailByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Teacher t WHERE t.academy.acaId = :acaId AND t.user.userId = :userId")
    Optional<Teacher> findByAcaIdAndUserId(@Param("acaId") Long acaId, @Param("userId") Long userId);
}