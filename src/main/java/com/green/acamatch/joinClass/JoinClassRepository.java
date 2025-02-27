package com.green.acamatch.joinClass;

import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinClassRepository extends JpaRepository<JoinClass, Long> {
    @Query("SELECT j.user FROM JoinClass j WHERE j.acaClass.classId = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);

    @Query("SELECT COUNT(*) FROM JoinClass a WHERE a.acaClass.classId = :classId AND a.user.userId = :userId")
    Long existsJoinClass(@Param("classId")Long classId, @Param("userId")Long userId);
}