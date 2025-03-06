package com.green.acamatch.joinClass;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.joinClass.JoinClass;
import com.green.acamatch.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JoinClassRepository extends JpaRepository<JoinClass, Long> {
    @Query("SELECT j.user FROM JoinClass j WHERE j.acaClass.classId = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);

    @Query("SELECT COUNT(*) FROM JoinClass a WHERE a.acaClass.classId = :classId AND a.user.userId = :userId")
    Long existsJoinClass(@Param("classId")Long classId, @Param("userId")Long userId);

    Optional<JoinClass> findByAcaClassAndUser(AcaClass acaClass, User user);

    // 특정 수업(AcaClass)과 학생(User) 간의 JoinClass 존재 여부 확인
    boolean existsByAcaClassAndUser(AcaClass acaClass, User user);


    Optional<JoinClass> findByAcaClass_ClassIdAndUser_UserId(Long classId, Long userId);


//    @Query("SELECT COUNT(j) > 0 FROM JoinClass j " +
//            "LEFT JOIN Relationship r ON j.user.userId = r.student.userId " +
//            "WHERE j.acaClass.classId = :classId " +
//            "AND (j.user.userId = :userId OR r.parent.userId = :userId) " +
//            "AND (r.certification = 1 OR r.certification IS NULL)")
//    boolean existsByAcaClass_ClassIdAndUser_UserId(@Param("classId") Long classId, @Param("userId") Long userId);

    boolean existsByAcaClass_ClassIdAndUser_UserId(Long classId, Long userId);

    @Query("""
        SELECT COUNT(j) > 0
        FROM JoinClass j
        LEFT JOIN Relationship r ON j.user.userId = r.student.userId
        WHERE j.acaClass.classId = :classId
        AND (:userId = j.user.userId OR (:userId = r.parent.userId AND r.certification = 1))
    """)
    boolean existsByClassAndStudentOrParent(@Param("classId") Long classId, @Param("userId") Long userId);




}