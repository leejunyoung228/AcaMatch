package com.green.acamatch.manager;

import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.manager.TeacherIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SnsTeacherRepository extends JpaRepository<Teacher, TeacherIds> {
    Optional<Teacher> findByTeacherIds(TeacherIds teacherIds);
}