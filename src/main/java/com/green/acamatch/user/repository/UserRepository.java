package com.green.acamatch.user.repository;

import com.green.acamatch.entity.acaClass.AcaClass;
import com.green.acamatch.entity.myenum.UserRole;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.user.model.UserReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    Optional<User> findByEmail(String email);


    Optional<User> findByUserIdAndUserRole(Long userId, UserRole userRole);

    Optional<User> findByUserId(long userId);

    boolean existsByUserIdAndUserRole(Long userId, UserRole role);

    boolean existsByUserId(long userId);

    @Query("SELECT u FROM User u JOIN JoinClass jc ON u.userId = jc.user.userId WHERE jc.acaClass = :acaClass")
    List<User> findStudentsByClass(@Param("acaClass") AcaClass acaClass);

    @Query("SELECT u.userId AS userId, u.userRole AS userRole, u.name AS name, " +
            "u.email AS email, u.phone AS phone, u.birth AS birth, " +
            "u.nickName AS nickName, u.userPic AS userPic, " +
            "u.createdAt AS createdAt, u.updatedAt AS updatedAt, COUNT(r) AS reportsCount " +
            "FROM User u LEFT JOIN Reports r ON u.userId = r.user.userId " +
            "GROUP BY u.userId")
    List<UserReportProjection> findUsersWithReportCount();

    @Query("SELECT u.userId AS userId, u.userRole AS userRole, u.name AS name, " +
            "u.email AS email, u.phone AS phone, u.birth AS birth, " +
            "u.nickName AS nickName, u.userPic AS userPic, " +
            "u.createdAt AS createdAt, u.updatedAt AS updatedAt, COUNT(r) AS reportsCount " +
            "FROM User u LEFT JOIN Reports r ON u.userId = r.user.userId " +
            "WHERE u.userId = :userId " +  // 특정 사용자 ID만 조회
            "GROUP BY u.userId")
    Optional<UserReportProjection> findUserWithReportCountById(@Param("userId") Long userId);

}
