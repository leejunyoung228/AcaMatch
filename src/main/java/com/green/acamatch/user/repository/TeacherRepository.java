package com.green.acamatch.user.repository;

import com.green.acamatch.entity.manager.Teacher;
import com.green.acamatch.entity.manager.TeacherIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, TeacherIds> {
}