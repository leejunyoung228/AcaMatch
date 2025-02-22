package com.green.acamatch.joinClass.model;

import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinClassRepository extends JpaRepository<JoinClass, Long> {
    @Query("SELECT j.user FROM JoinClass j WHERE j.classId = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);
}
